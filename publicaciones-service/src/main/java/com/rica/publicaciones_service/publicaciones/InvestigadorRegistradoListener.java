package com.rica.publicaciones_service.publicaciones;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class InvestigadorRegistradoListener {

    @KafkaListener(topics = "investigadores.registrados", groupId = "publicaciones-group")
    public void escuchar(String payload) {
        System.out.println("[publicaciones-service] Nuevo investigador disponible: " + payload);
    }
}
