package com.rica.publicaciones_service.publicaciones;

import org.springframework.stereotype.Service;

import com.rica.publicaciones_service.compartido.RecursoNoEncontradoException;

import java.util.List;

@Service
public class PublicacionService {

    private final PublicacionRepository publicacionRepository;
    private final VerificadorInvestigador verificadorInvestigador;
    private final LimitePublicacionesAnualesService limitePublicacionesService;

    public PublicacionService(PublicacionRepository publicacionRepository,
            VerificadorInvestigador verificadorInvestigador,
            LimitePublicacionesAnualesService limitePublicacionesService) {
        this.publicacionRepository = publicacionRepository;
        this.verificadorInvestigador = verificadorInvestigador;
        this.limitePublicacionesService = limitePublicacionesService;
    }

    public Publicacion registrar(Publicacion publicacion) {
        // Validación 1: Verificar existencia del investigador mediante el nuevo puerto
        if (!verificadorInvestigador.existe(publicacion.getInvestigadorCorreo())) {
            throw new RecursoNoEncontradoException(
                    "No existe un investigador con correo " + publicacion.getInvestigadorCorreo());
        }

        // Validación 2: Verificar si cumple la regla de límite de publicaciones
        if (!limitePublicacionesService.puedeRegistrar(publicacion)) {
            throw new LimiteAnualExcedidoException(
                    "El investigador con correo " + publicacion.getInvestigadorCorreo() +
                            " ha superado el límite de publicaciones permitidas para este año.");
        }

        // Si pasa ambas validaciones, guardamos la publicación
        return publicacionRepository.save(publicacion);
    }

    public List<Publicacion> listarPorInvestigador(String investigadorCorreo) {
        return publicacionRepository.findByInvestigadorCorreo(investigadorCorreo);
    }

    public Publicacion buscarPorId(String id) {
        return publicacionRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe una publicación con id " + id));
    }
}
