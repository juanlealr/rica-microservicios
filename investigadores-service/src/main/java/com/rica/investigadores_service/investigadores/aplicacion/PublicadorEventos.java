package com.rica.investigadores_service.investigadores.aplicacion;

import java.util.Map;

/**
 * Puerto de salida (Hexagonal Architecture) para publicar eventos de dominio.
 * Permite que los casos de uso publiquen eventos sin acoplarse a la
 * infraestructura (Outbox, Kafka, etc.).
 */
public interface PublicadorEventos {

    void publicar(String topico, String clave, Map<String, Object> contenido);

}
