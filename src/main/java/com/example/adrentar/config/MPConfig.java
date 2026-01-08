package com.example.adrentar.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import com.mercadopago.MercadoPagoConfig;
import jakarta.annotation.PostConstruct;

@Configuration
public class MPConfig {

    @Value("${mercadopago.access.token}")
    private String accessToken;

    @Value("${mercadopago.integrator_id:}")
    private String integratorId;

    @PostConstruct
    public void initMercadoPago() {

        MercadoPagoConfig.setAccessToken(accessToken);

        if (!integratorId.isEmpty()) {
            MercadoPagoConfig.setIntegratorId(integratorId);
        }

        System.out.println("⚡ Mercado Pago inicializado correctamente");
    }
}
