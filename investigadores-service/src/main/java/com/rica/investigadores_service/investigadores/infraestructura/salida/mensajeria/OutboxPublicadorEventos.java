package com.rica.investigadores_service.investigadores.infraestructura.salida.mensajeria;

import com.rica.investigadores_service.investigadores.aplicacion.PublicadorEventos;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

@Component
public class OutboxPublicadorEventos implements PublicadorEventos {

    private final OutboxEventRepository repositorio;
    private final ObjectMapper objectMapper;

    public OutboxPublicadorEventos(OutboxEventRepository repositorio, ObjectMapper objectMapper) {
        this.repositorio = repositorio;
        this.objectMapper = objectMapper;
    }

    @Override
    public void publicar(String topico, String clave, Map<String, Object> contenido) {
        Map<String, Object> conTraza = new HashMap<>(contenido);

        // Agrega el id de correlación si existe en el MDC
        String correlationId = MDC.get("correlationId");
        if (correlationId != null) {
            conTraza.put("correlationId", correlationId);
        }

        // Guarda la clave dentro del mapa del evento si la necesitas al enviarlo a
        // Kafka
        if (clave != null) {
            conTraza.put("clave", clave);
        }

        try {
            String payload = objectMapper.writeValueAsString(conTraza);

            // Se usa el constructor actual: (tipo, payload, publicado = false)
            repositorio.save(new OutboxEvent(topico, payload, false));
        } catch (Exception e) {
            throw new RuntimeException("Error al serializar el evento para la tabla Outbox", e);
        }
    }
}