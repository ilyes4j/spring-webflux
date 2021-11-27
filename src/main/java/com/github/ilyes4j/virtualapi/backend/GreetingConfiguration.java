package com.github.ilyes4j.virtualapi.backend;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@EnableWebFlux
@Configuration(proxyBeanMethods = false)
public class GreetingConfiguration {

    @Bean
    public GreetingService getGreetingService() {
        return new GreetingService();
    }

    @Bean
    public GreetingController getGreetingHandler(GreetingService service) {
        return new GreetingController(service);
    }

    @Bean
    public RouterFunction<ServerResponse> route(GreetingController greetingController) {

        return RouterFunctions.route(
                GET("/hello").and(accept(MediaType.APPLICATION_JSON)),
                greetingController::hello
        );
    }
}
