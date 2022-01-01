package com.github.ilyes4j.virtualapi.backend;

import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
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
    public Mono<ServerResponse> mock(ServerRequest request) {
        return storage.fetchContentAsync(requestToFileName(request));
    }

    private String requestToFileName(ServerRequest request) {

        StringBuilder sb = new StringBuilder(request.path());

        MultiValueMap<String, String> params = request.queryParams();

        if (!params.isEmpty()) {
            sb.append('/');
            request.queryParams().forEach((s, strings) -> {
                sb.append(s);
                sb.append('=');
                sb.append(strings.get(0));
            });
        }
        sb.append(".json");
        return sb.toString();
    }
}
