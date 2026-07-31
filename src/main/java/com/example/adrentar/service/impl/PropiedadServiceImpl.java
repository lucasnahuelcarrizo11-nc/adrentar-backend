package com.example.adrentar.service.impl;

import com.example.adrentar.entity.Propiedad;
import com.example.adrentar.repository.PropiedadRepository;
import com.example.adrentar.service.PropiedadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class PropiedadServiceImpl implements PropiedadService {

    @Autowired
private PropiedadRepository propiedadRepository;

    public PropiedadServiceImpl(PropiedadRepository propiedadRepository) {
        this.propiedadRepository = propiedadRepository;
    }

    @Override
    public Propiedad crearPropiedad(Propiedad propiedad) {

    System.out.println("Objeto recibido: " + propiedad);
    System.out.println("Dirección: " + propiedad.getDireccion());

        if (propiedadRepository.existsByDireccion(propiedad.getDireccion())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La dirección ya existe"
            );
        }
        return propiedadRepository.save(propiedad);
    }

    @Override
    public Propiedad buscarPorDireccion(String direccion) {
        return null;
    }

    @Override
    public List<Propiedad> listarPropiedades() {
        return propiedadRepository.findAll();
    }

    @Override
    public Optional<Propiedad> obtenerPropiedadPorId(Long id) {
        return propiedadRepository.findById(id);
    }

    @Override
    public Propiedad actualizarPropiedad(Long idPropiedad, Propiedad propiedad) {
        Propiedad existente = propiedadRepository.findById(idPropiedad)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Propiedad no encontrada"
                ));

        if (propiedadRepository.existsByDireccionIgnoreCaseAndIdPropiedadNot(
                propiedad.getDireccion(),
                idPropiedad)) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La dirección ya existe"
            );
        }

        // ✅ Actualizar campos
        existente.setTituloPropiedad(propiedad.getTituloPropiedad());
        existente.setDireccion(propiedad.getDireccion());
        existente.setEstado(propiedad.getEstado());
        existente.setAmbientes(propiedad.getAmbientes());
        existente.setLatitud(propiedad.getLatitud());
        existente.setLongitud(propiedad.getLongitud());

        return propiedadRepository.save(existente);
    }

    @Override
    public void eliminarPropiedad(Long idPropiedad) {
    Optional <Propiedad> propiedad = propiedadRepository.findById(idPropiedad);
    if (!propiedad.isPresent()) {
        throw new IllegalArgumentException("Propiedad no encontrada");
    }
    propiedadRepository.deleteById(idPropiedad);
    }

    @Override
    public List<Propiedad> listarPropiedadesPorPropietario(Long propietarioId) {
        return propiedadRepository.findByPropietarioIdUsuario(propietarioId);
    }

    public List<Propiedad> listarTodas() {
        return propiedadRepository.findAll();
    }
}
