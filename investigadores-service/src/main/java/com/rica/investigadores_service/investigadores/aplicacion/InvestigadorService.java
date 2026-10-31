package com.rica.investigadores_service.investigadores.aplicacion;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rica.investigadores_service.compartido.RecursoNoEncontradoException;
import com.rica.investigadores_service.investigadores.dominio.Investigador;
import com.rica.investigadores_service.investigadores.infraestructura.salida.mensajeria.OutboxEvent;
import com.rica.investigadores_service.investigadores.infraestructura.salida.mensajeria.OutboxEventRepository;

@Service
public class InvestigadorService implements InvestigadorUseCase {

    private final RepositorioInvestigadores repositorioInvestigadores;
    private final InvestigadorFactory investigadorFactory;
    private final OutboxEventRepository outboxRepository;

    public InvestigadorService(RepositorioInvestigadores repositorioInvestigadores,
            InvestigadorFactory investigadorFactory,
            OutboxEventRepository outboxRepository) {
        this.repositorioInvestigadores = repositorioInvestigadores;
        this.investigadorFactory = investigadorFactory;
        this.outboxRepository = outboxRepository;
    }

    @Override
    public List<Investigador> listarTodos() {
        return repositorioInvestigadores.listarTodos();
    }

    @Override
    public Investigador buscarPorId(Long id) {
        return repositorioInvestigadores.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un investigador con id " + id));
    }

    @Override
    @Transactional
    public Investigador registrar(String nombreCompleto, String correoInstitucional, String grupoInvestigacion) {
        Investigador investigador = repositorioInvestigadores.guardar(
                investigadorFactory.crear(nombreCompleto, correoInstitucional, grupoInvestigacion));

        String payload = """
                {"correoInstitucional": "%s", "nombreCompleto": "%s"}""".formatted(
                investigador.getCorreoInstitucional().valor(), investigador.getNombreCompleto());
        outboxRepository.save(new OutboxEvent("InvestigadorRegistrado", payload, false));

        return investigador;
    }

    @Transactional
    public void iniciarEliminacion(String correoInstitucional) {
        Investigador investigador = repositorioInvestigadores.buscarPorCorreo(correoInstitucional)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe: " + correoInstitucional));
        investigador.marcarEnEliminacion();
        repositorioInvestigadores.guardar(investigador);

        String payload = """
                {"correoInstitucional": "%s"}""".formatted(correoInstitucional);
        outboxRepository.save(new OutboxEvent("InvestigadorEnEliminacion", payload, false));
    }

    @Override
    @Transactional
    public void confirmarEliminacion(String correoInstitucional) {
        repositorioInvestigadores.buscarPorCorreo(correoInstitucional)
                .filter(Investigador::estaEnEliminacion)
                .ifPresent(investigador -> {
                    repositorioInvestigadores.eliminar(investigador);

                    String payload = """
                            {"correoInstitucional": "%s"}""".formatted(correoInstitucional);
                    outboxRepository.save(new OutboxEvent("InvestigadorEliminado", payload, false));
                });
    }
}
