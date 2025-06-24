package com.epam.javadvanced.etl.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import com.epam.javadvanced.etl.handler.SportHandler;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
@RequiredArgsConstructor
public class RouterConfig {

    private final SportHandler sportHandler;

    @Bean
    public RouterFunction<ServerResponse> sportRoutes() {
        return RouterFunctions
                .route(POST("/api/v1/sport/{sportname}"), sportHandler::createSport)
                .andRoute(GET("/api/v1/sport"), sportHandler::searchSports);
    }
}