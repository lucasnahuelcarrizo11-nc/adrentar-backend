package com.example.adrentar.service.impl;

import com.example.adrentar.dto.SuscripcionStatusDto;
import com.example.adrentar.entity.EstadoSuscripcion;
import com.example.adrentar.entity.Suscripcion;
import com.example.adrentar.entity.Usuario;
import com.example.adrentar.repository.SuscripcionRepository;
import com.example.adrentar.repository.UsuarioRepository;
import com.example.adrentar.service.SuscripcionService;
import com.mercadopago.client.preapproval.PreapprovalClient;
import com.mercadopago.client.preapproval.PreapprovalCreateRequest;
import com.mercadopago.client.preapproval.PreapprovalUpdateRequest;
import com.mercadopago.client.preapproval.PreApprovalAutoRecurringCreateRequest;
import com.mercadopago.resources.preapproval.Preapproval;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Nombres de clase verificados contra la documentación oficial del SDK
 * (com.mercadopago:sdk-java, familia 2.1.x): el paquete usa "Preapproval"
 * (con minúscula en "approval"), salvo la clase de recurrencia automática
 * que sí lleva "PreApproval" con mayúscula — es una inconsistencia real
 * del propio SDK, no un error de tipeo acá.
 */
@Service
public class SuscripcionServiceImpl implements SuscripcionService {

    private static final int DIAS_TRIAL = 30;
    private static final BigDecimal PRECIO_MENSUAL = BigDecimal.valueOf(5000);

    private final SuscripcionRepository suscripcionRepository;
    private final UsuarioRepository usuarioRepository;

    @Value("${frontend.url}")
    private String frontendUrl;

    public SuscripcionServiceImpl(SuscripcionRepository suscripcionRepository,
                                  UsuarioRepository usuarioRepository) {
        this.suscripcionRepository = suscripcionRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public void iniciarTrial(Usuario usuario) {
        if (suscripcionRepository.findByUsuario_IdUsuario(usuario.getIdUsuario()).isPresent()) {
            return; // ya tiene una suscripción/trial registrado, no duplicar
        }

        Suscripcion suscripcion = new Suscripcion();
        suscripcion.setUsuario(usuario);
        suscripcion.setEstado(EstadoSuscripcion.TRIAL);
        suscripcion.setFechaInicioTrial(LocalDate.now());
        suscripcion.setFechaFinTrial(LocalDate.now().plusDays(DIAS_TRIAL));
        suscripcionRepository.save(suscripcion);
    }

    @Override
    public SuscripcionStatusDto obtenerEstado(Long idUsuario) {
        Suscripcion suscripcion = suscripcionRepository.findByUsuario_IdUsuario(idUsuario)
                .orElseThrow(() -> new RuntimeException("El usuario no tiene una suscripción/trial registrado"));

        actualizarEstadoSiVencioTrial(suscripcion);

        long diasRestantes = 0;
        if (suscripcion.getEstado() == EstadoSuscripcion.TRIAL) {
            diasRestantes = ChronoUnit.DAYS.between(LocalDate.now(), suscripcion.getFechaFinTrial());
            if (diasRestantes < 0) diasRestantes = 0;
        }

        return new SuscripcionStatusDto(
                suscripcion.getEstado(),
                suscripcion.getFechaFinTrial(),
                diasRestantes,
                calcularAccesoActivo(suscripcion),
                suscripcion.getFechaProximoPago()
        );
    }

    @Override
    public boolean tieneAccesoActivo(Long idUsuario) {
        Suscripcion suscripcion = suscripcionRepository.findByUsuario_IdUsuario(idUsuario).orElse(null);
        if (suscripcion == null) return false;

        actualizarEstadoSiVencioTrial(suscripcion);
        return calcularAccesoActivo(suscripcion);
    }

    private boolean calcularAccesoActivo(Suscripcion suscripcion) {
        if (suscripcion.getEstado() == EstadoSuscripcion.ACTIVA) return true;
        if (suscripcion.getEstado() == EstadoSuscripcion.TRIAL) {
            return !LocalDate.now().isAfter(suscripcion.getFechaFinTrial());
        }
        return false;
    }

    private void actualizarEstadoSiVencioTrial(Suscripcion suscripcion) {
        if (suscripcion.getEstado() == EstadoSuscripcion.TRIAL
                && LocalDate.now().isAfter(suscripcion.getFechaFinTrial())) {
            suscripcion.setEstado(EstadoSuscripcion.VENCIDA);
            suscripcionRepository.save(suscripcion);
        }
    }

    @Override
    public String crearSuscripcionMP(Long idUsuario) throws Exception {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Suscripcion suscripcion = suscripcionRepository.findByUsuario_IdUsuario(idUsuario)
                .orElseThrow(() -> new RuntimeException("El usuario no tiene un trial/suscripción registrado"));

        if (suscripcion.getEstado() == EstadoSuscripcion.ACTIVA) {
            throw new RuntimeException("El usuario ya tiene una suscripción activa");
        }

        PreApprovalAutoRecurringCreateRequest autoRecurring = PreApprovalAutoRecurringCreateRequest.builder()
                .frequency(1)
                .frequencyType("months")
                .transactionAmount(PRECIO_MENSUAL)
                .currencyId("ARS")
                .build();

        PreapprovalCreateRequest request = PreapprovalCreateRequest.builder()
                .reason("Suscripción mensual Adrentar")
                .externalReference("suscripcion-" + idUsuario)
                .payerEmail(usuario.getEmail())
                .backUrl(frontendUrl + "/mi-suscripcion")
                .autoRecurring(autoRecurring)
                .status("pending")
                .build();

        PreapprovalClient client = new PreapprovalClient();
        Preapproval preapproval;

        try {
            preapproval = client.create(request);

            System.out.println("================================");
            System.out.println("PREAPPROVAL CREADA");
            System.out.println("ID: " + preapproval.getId());
            System.out.println("STATUS: " + preapproval.getStatus());
            System.out.println("INIT POINT: " + preapproval.getInitPoint());
            System.out.println("================================");

        } catch (com.mercadopago.exceptions.MPApiException e) {
            System.out.println("❌ ERROR MERCADO PAGO (preapproval)");
            System.out.println("STATUS CODE: " + e.getStatusCode());
            System.out.println("RESPONSE BODY: " + e.getApiResponse().getContent());
            throw e;
        }

        suscripcion.setPreapprovalId(preapproval.getId());
        suscripcion.setEstado(EstadoSuscripcion.PENDIENTE_AUTORIZACION);
        suscripcionRepository.save(suscripcion);

        return preapproval.getInitPoint();
    }

    @Override
    public void procesarWebhookPreapproval(String preapprovalId) throws Exception {

        System.out.println("========================================");
        System.out.println("Entró a procesarWebhookPreapproval()");
        System.out.println("Preapproval recibido: " + preapprovalId);

        PreapprovalClient client = new PreapprovalClient();

        System.out.println("Consultando Mercado Pago...");

        Preapproval preapproval = client.get(preapprovalId);

        System.out.println("Respuesta obtenida.");

        System.out.println("ID: " + preapproval.getId());
        System.out.println("STATUS: " + preapproval.getStatus());

        Suscripcion suscripcion = suscripcionRepository
                .findByPreapprovalId(preapprovalId)
                .orElseThrow(() -> new RuntimeException(
                        "No existe una suscripción con ese preapprovalId"));

        System.out.println("Suscripción encontrada en BD.");
        System.out.println("Estado actual: " + suscripcion.getEstado());

        String status = preapproval.getStatus();

        switch (status) {

            case "authorized":

                System.out.println("Estado AUTHORIZED");

                suscripcion.setEstado(EstadoSuscripcion.ACTIVA);
                suscripcion.setFechaProximoPago(LocalDate.now().plusMonths(1));
                break;

            case "paused":

                System.out.println("Estado PAUSED");

                suscripcion.setEstado(EstadoSuscripcion.VENCIDA);
                break;

            case "cancelled":

                System.out.println("Estado CANCELLED");

                suscripcion.setEstado(EstadoSuscripcion.CANCELADA);
                break;

            default:

                System.out.println("Estado NO MANEJADO: " + status);
                break;
        }

        System.out.println("Guardando en BD...");

        suscripcionRepository.save(suscripcion);

        System.out.println("Guardado correctamente.");

        System.out.println("========================================");
    }

    @Override
    public void cancelarSuscripcion(Long idUsuario) throws Exception {
        Suscripcion suscripcion = suscripcionRepository.findByUsuario_IdUsuario(idUsuario)
                .orElseThrow(() -> new RuntimeException("El usuario no tiene una suscripción registrada"));

        if (suscripcion.getPreapprovalId() != null) {
            PreapprovalClient client = new PreapprovalClient();
            PreapprovalUpdateRequest cancelRequest = PreapprovalUpdateRequest.builder()
                    .status("cancelled")
                    .build();
            client.update(suscripcion.getPreapprovalId(), cancelRequest);
        }

        suscripcion.setEstado(EstadoSuscripcion.CANCELADA);
        suscripcionRepository.save(suscripcion);
    }

    @Override
    public void sincronizarEstadoSuscripcion(Long idUsuario) throws Exception {

        Suscripcion suscripcion = suscripcionRepository
                .findByUsuario_IdUsuario(idUsuario)
                .orElseThrow(() -> new RuntimeException("Suscripción no encontrada"));

        if (suscripcion.getPreapprovalId() == null) {
            return;
        }

        procesarWebhookPreapproval(suscripcion.getPreapprovalId());
    }
}