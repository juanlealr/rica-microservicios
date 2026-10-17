package com.rica.investigadores_service.investigadores.infraestructura.salida.mensajeria;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "outbox_event")
public class OutboxEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tipo;
    private String payload;
    private boolean publicado;

    protected OutboxEvent() {
    } // requerido por JPA

    public OutboxEvent(String tipo, String payload, boolean publicado) {
        this.tipo = tipo;
        this.payload = payload;
        this.publicado = publicado;
    }

    public Long getId() {
        return id;
    }

    public String getTipo() {
        return tipo;
    }

    public String getPayload() {
        return payload;
    }

    public boolean isPublicado() {
        return publicado;
    }

    public void setPublicado(boolean publicado) {
        this.publicado = publicado;
    }
}
