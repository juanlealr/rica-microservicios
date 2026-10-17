package com.rica.investigadores_service.investigadores.infraestructura.salida.mensajeria;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class OutboxPublisher {

    private final OutboxEventRepository outboxRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public OutboxPublisher(OutboxEventRepository outboxRepository,
            KafkaTemplate<String, String> kafkaTemplate) {
        this.outboxRepository = outboxRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Scheduled(fixedDelay = 2000)
    @Transactional
    public void publicarPendientes() {
        for (var evento : outboxRepository.findByPublicadoFalse()) {
            String topic = switch (evento.getTipo()) {
                case "InvestigadorRegistrado" -> "investigadores.registrados";
                case "InvestigadorEnEliminacion" -> "investigadores.en-eliminacion";
                default -> throw new IllegalStateException("Tipo desconocido: " + evento.getTipo());
            };
            kafkaTemplate.send(topic, evento.getPayload());
            evento.setPublicado(true);
            outboxRepository.save(evento);
        }
    }
}
