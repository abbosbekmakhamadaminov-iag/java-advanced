package com.epam.javadvanced.config;

import com.epam.javadvanced.service.PingPongServiceImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class GrpcConfig {
    private final PingPongServiceImpl pongService;

    @Value("${grpc.server.port:8080}")
    private Integer port;

    private Server server;

    @PostConstruct
    public void startGrpcServer() {
        try {
            server = ServerBuilder
                    .forPort(8080)
                    .addService(pongService)
                    .build();
            server.start();
            log.info("gRPC Server started, listening on port {}", port);
            log.info("Avaliable services: {}", server.getServices().toString());

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                log.info("Shutting down gRPC server");
                if (server != null) {
                    server.shutdown();
                }
            }));
        } catch (IOException e) {
            log.error("Failed to start gRPC server on port {}", port, e);
            throw new RuntimeException("Failed to start gRPC server", e);
        }
    }

    @PreDestroy
    public void stopGrpcServer() {
        if (server != null) {
            server.shutdown();
            log.info("gRPC server shutdown");
        }
    }


    @Bean
    public Server grpcServer() {
        return server;
    }
}
