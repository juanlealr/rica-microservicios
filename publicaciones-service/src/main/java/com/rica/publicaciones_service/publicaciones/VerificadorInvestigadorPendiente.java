package com.rica.publicaciones_service.publicaciones;

import org.springframework.stereotype.Component;

@Component
public class VerificadorInvestigadorPendiente implements VerificadorInvestigador {

    @Override
    public boolean existe(String correoInstitucional) {
        // Lógica para verificar si el investigador existe en la base de datos
        // Por ahora, devolvemos false para indicar que no existe
        return true;
    }

}
