package com.github.ilyes4j.virtualapi.backend;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.io.IOException;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@EnableWebFlux
@Configuration(proxyBeanMethods = false)
public class MockConfiguration {

    @Bean
    public GoogleCloudStorage getGoogleCloudStorage() throws IOException {
        return new GoogleCloudStorage("virtual-api-com-db");
    }

    @Bean
    public MockController getGreetingHandler(GoogleCloudStorage storage) {
        return new MockController(storage);
    }

    @Bean
    public RouterFunction<ServerResponse> route(MockController mockController) {

        return RouterFunctions.route(
                GET("/rest/**").and(accept(MediaType.APPLICATION_JSON)),
                mockController::hello
        );
    }
}
