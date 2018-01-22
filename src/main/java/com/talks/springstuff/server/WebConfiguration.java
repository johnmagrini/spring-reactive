package com.talks.springstuff.server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.*;

@Configuration
public class WebConfiguration {

    @Bean
    RouterFunction<?> routes (InstrumentService is) {
        return
                RouterFunctions
                .route(RequestPredicates.GET("/instruments"),
                        serverRequest -> ServerResponse.ok().body(is.getAll(), Instrument.class))
                .andRoute(RequestPredicates.GET("/instruments/{id}"),
                        serverRequest -> ServerResponse.ok().body(is.getById(serverRequest.pathVariable("id")), Instrument.class));
    }
}
