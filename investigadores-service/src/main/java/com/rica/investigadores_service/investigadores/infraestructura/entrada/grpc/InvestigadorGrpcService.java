package com.rica.investigadores_service.investigadores.infraestructura.entrada.grpc;

import org.springframework.grpc.server.service.GrpcService;

import com.rica.investigadores_service.investigadores.aplicacion.RepositorioInvestigadores;
import io.grpc.stub.StreamObserver;

// IMPORTS CORREGIDOS (con .grpc al final)
import com.rica.investigadores_service.grpc.InvestigadorGrpcGrpc;
import com.rica.investigadores_service.grpc.InvestigadorRequest;
import com.rica.investigadores_service.grpc.InvestigadorResponse;

@GrpcService
public class InvestigadorGrpcService extends InvestigadorGrpcGrpc.InvestigadorGrpcImplBase {

    private final RepositorioInvestigadores repositorio;

    InvestigadorGrpcService(RepositorioInvestigadores repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public void existeInvestigador(InvestigadorRequest request, StreamObserver<InvestigadorResponse> responseObserver) {
        boolean existe = repositorio.existeCorreo(request.getCorreoInstitucional());

        InvestigadorResponse response = InvestigadorResponse.newBuilder()
                .setExiste(existe)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
