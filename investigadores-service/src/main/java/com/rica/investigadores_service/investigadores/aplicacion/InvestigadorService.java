package com.rica.investigadores_service.investigadores.aplicacion;

import org.springframework.stereotype.Service;
import com.rica.investigadores_service.compartido.RecursoNoEncontradoException;
import com.rica.investigadores_service.investigadores.dominio.Investigador;

import java.util.List;

@Service
public class InvestigadorService implements InvestigadorUseCase {

    private final RepositorioInvestigadores repositorioInvestigadores;
    private final InvestigadorFactory investigadorFactory;

    public InvestigadorService(RepositorioInvestigadores repositorioInvestigadores,
            InvestigadorFactory investigadorFactory) {
        this.repositorioInvestigadores = repositorioInvestigadores;
        this.investigadorFactory = investigadorFactory;
    }

    public List<Investigador> listarTodos() {
        return repositorioInvestigadores.listarTodos();
    }

    public Investigador buscarPorId(Long id) {
        // Se asume que repositorioInvestigadores.buscarPorId(id) devuelve un
        // Optional<Investigador>
        return repositorioInvestigadores.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un investigador con id " + id));
    }

    public Investigador registrar(String nombreCompleto, String correoInstitucional, String grupoInvestigacion) {
        Investigador investigador = investigadorFactory.crear(nombreCompleto, correoInstitucional, grupoInvestigacion);
        return repositorioInvestigadores.guardar(investigador);
    }
}
