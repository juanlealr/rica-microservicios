package com.rica.publicaciones_service.publicaciones;

import org.springframework.stereotype.Service;

@Service
public class LimitePublicacionesAnualesService {

    private static final int MAXIMO_POR_ANIO = 5;

    private final PublicacionRepository publicacionRepository;

    public LimitePublicacionesAnualesService(PublicacionRepository publicacionRepository) {
        this.publicacionRepository = publicacionRepository;
    }

    // Ahora recibe solo la Publicación
    public boolean puedeRegistrar(Publicacion nueva) {
        long registradasEsteAnio = publicacionRepository.countByInvestigadorCorreoAndAnio(
                nueva.getInvestigadorCorreo(), nueva.getAnio());
        return registradasEsteAnio < MAXIMO_POR_ANIO;
    }
}
