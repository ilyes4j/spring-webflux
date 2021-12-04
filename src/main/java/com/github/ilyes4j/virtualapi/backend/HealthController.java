package com.github.ilyes4j.virtualapi.backend;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;

@Component
public class HealthController {

    @Nonnull
    public Mono<ServerResponse> health(ServerRequest ignoredRequest) {
        return ServerResponse.ok().build();
    }
}
