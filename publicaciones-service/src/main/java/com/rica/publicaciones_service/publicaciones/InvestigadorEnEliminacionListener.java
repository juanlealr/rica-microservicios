package com.rica.publicaciones_service.publicaciones;

import java.util.List;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import tools.jackson.databind.ObjectMapper;

@Component
public class InvestigadorEnEliminacionListener {

  private final PublicacionRepository publicacionRepository;
  private final KafkaTemplate<String, String> kafkaTemplate;
  private final ObjectMapper objectMapper;

  public InvestigadorEnEliminacionListener(PublicacionRepository publicacionRepository,
      KafkaTemplate<String, String> kafkaTemplate,
      ObjectMapper objectMapper) {
    this.publicacionRepository = publicacionRepository;
    this.kafkaTemplate = kafkaTemplate;
    this.objectMapper = objectMapper;
  }

  @KafkaListener(topics = "investigadores.en-eliminacion", groupId = "publicaciones-group")
  public void escuchar(String payload) {
    String correo = objectMapper.readTree(payload).get("correoInstitucional").asString();

    List<Publicacion> huerfanas = publicacionRepository.findByInvestigadorCorreo(correo);
    huerfanas.forEach(Publicacion::marcarComoHuerfana);
    publicacionRepository.saveAll(huerfanas);

    kafkaTemplate.send("publicaciones.marcadas-huerfanas",
        """
            {"correoInstitucional": "%s", "cantidad": %d}""".formatted(correo, huerfanas.size()));
  }
}
