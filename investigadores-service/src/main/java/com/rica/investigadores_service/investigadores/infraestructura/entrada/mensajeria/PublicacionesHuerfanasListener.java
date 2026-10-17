package com.rica.investigadores_service.investigadores.infraestructura.entrada.mensajeria;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rica.investigadores_service.investigadores.aplicacion.InvestigadorService;

@Component
public class PublicacionesHuerfanasListener {

  private final InvestigadorService investigadorService;
  private final ObjectMapper objectMapper;

  public PublicacionesHuerfanasListener(InvestigadorService investigadorService,
      ObjectMapper objectMapper) {
    this.investigadorService = investigadorService;
    this.objectMapper = objectMapper;
  }

  @KafkaListener(topics = "publicaciones.marcadas-huerfanas", groupId = "investigadores-group")
  public void escuchar(String payload) throws Exception {
    JsonNode json = objectMapper.readTree(payload);
    String correo = json.get("correoInstitucional").asText();
    investigadorService.confirmarEliminacion(correo);
  }
}
