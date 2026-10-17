package com.rica.publicaciones_service.publicaciones;

import org.springframework.stereotype.Component;

import com.rica.investigadores_service.grpc.InvestigadorGrpcGrpc.InvestigadorGrpcBlockingStub;
import com.rica.investigadores_service.grpc.InvestigadorRequest;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;

@Component
public class VerificadorInvestigadorGrpc implements VerificadorInvestigador {

    private final InvestigadorGrpcBlockingStub stub;
    private final CircuitBreaker circuitBreaker;

    public VerificadorInvestigadorGrpc(InvestigadorGrpcBlockingStub stub,
                                       CircuitBreakerRegistry registry) {
        this.stub = stub;
        this.circuitBreaker = registry.circuitBreaker("investigadores-service");
    }

    @Override
    public boolean existe(String correoInstitucional) {
        try {
            return circuitBreaker.executeSupplier(() -> {
                var request = InvestigadorRequest.newBuilder()
                        .setCorreoInstitucional(correoInstitucional)
                        .build();
                return stub.existeInvestigador(request).getExiste();
            });
        } catch (Exception e) {
            return false;
        }
    }
}
