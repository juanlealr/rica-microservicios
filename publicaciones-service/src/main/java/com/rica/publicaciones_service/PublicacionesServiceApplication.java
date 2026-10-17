package com.rica.publicaciones_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.grpc.client.ImportGrpcClients;

import com.rica.investigadores_service.grpc.InvestigadorGrpcGrpc;

@ImportGrpcClients(target = "investigadores", types = InvestigadorGrpcGrpc.InvestigadorGrpcBlockingStub.class)
@SpringBootApplication
public class PublicacionesServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PublicacionesServiceApplication.class, args);
	}
}
