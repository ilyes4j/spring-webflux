package com.github.ilyes4j.virtualapi.backend;

import com.google.auth.Credentials;
import com.google.cloud.storage.StorageOptions;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class GoogleCloudStorage {

    private static final String CLOUD_STORAGE_URL = "https://storage.googleapis.com/";

    private final WebClient webClient;

    public GoogleCloudStorage(String bucketId) throws IOException {
        StorageOptions options = StorageOptions.newBuilder().build();

        Credentials credentials = options.getScopedCredentials();

        String dlPartialUrl = CLOUD_STORAGE_URL + bucketId;

        Map<String, List<String>> headers = credentials.getRequestMetadata(null);
        WebClient.Builder builder = WebClient.builder().baseUrl(dlPartialUrl);
        headers.forEach((key, value) -> builder.defaultHeader(key, value.toArray(new String[0])));
        webClient = builder.build();
    }

    public Mono<ServerResponse> fetchContentAsync(String rawMetadata) {

        return webClient.method(HttpMethod.GET)
                .uri(rawMetadata)
                .exchangeToMono(clientResponse -> clientResponse.bodyToMono(DataBuffer.class))
                .flatMap(s -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(s)));
    }
}
