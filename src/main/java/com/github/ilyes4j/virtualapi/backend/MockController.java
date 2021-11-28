package com.github.ilyes4j.virtualapi.backend;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;

@Component
public class MockController {

    private final GoogleCloudStorage storage;

    public MockController(GoogleCloudStorage storage) {
        this.storage = storage;
    }

    @Nonnull
    public Mono<ServerResponse> hello(ServerRequest request) {
        return storage.fetchContentAsync(request.path());
    }
}
