package com.github.ilyes4j.virtualapi.backend;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.server.*;

@EnableWebFlux
@Configuration(proxyBeanMethods = false)
public class MockConfiguration {

    @Bean
    public GoogleCloudStorage getGoogleCloudStorage(@Value("${maxClientCnx:500}") int maxClientCnx) {
        return new GoogleCloudStorage("virtual-api-com-db", maxClientCnx);
    }

    @Bean
    public MockController getMockController(GoogleCloudStorage storage) {
        return new MockController(storage);
    }

    @Bean
    public HealthController getHealthController() {
        return new HealthController();
    }

    @Bean
    public RouterFunction<ServerResponse> getHealthRoute(HealthController healthController) {

        return RouterFunctions.route(RequestPredicates.path("/_ah/start"), healthController::health);
    }

    @Bean
    public RouterFunction<ServerResponse> getMocksRoute(MockController mockController) {

        return RouterFunctions.route(RequestPredicates.path("/**"), mockController::mock);
    }
}
