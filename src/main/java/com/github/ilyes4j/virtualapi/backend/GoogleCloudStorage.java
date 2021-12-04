package com.github.ilyes4j.virtualapi.backend;

import com.google.auth.Credentials;
import com.google.cloud.storage.StorageOptions;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.*;

public class GoogleCloudStorage {

    @SuppressWarnings("HttpUrlsUsage")
    private static final String CLOUD_STORAGE_URL = "http://storage.googleapis.com/";

    private final WebClient webClient;

    private final Credentials credentials;

    public GoogleCloudStorage(String bucketId) {
        StorageOptions options = StorageOptions.newBuilder().build();

        credentials = options.getScopedCredentials();

        String dlPartialUrl = CLOUD_STORAGE_URL + bucketId;

        WebClient.Builder builder = WebClient.builder().baseUrl(dlPartialUrl);
        webClient = builder.build();
    }

    public Mono<ServerResponse> fetchContentAsync(String rawMetadata) {

        Map<String, List<String>> headers = getRequestMetadata();

        //noinspection deprecation
        return webClient.method(HttpMethod.GET)
                .uri(rawMetadata)
                .headers(httpHeaders -> headers.forEach(httpHeaders::addAll))
                .exchange()
                .flatMap(clientResponse -> {

                            ServerResponse.BodyBuilder builder = ServerResponse.status(clientResponse.statusCode());

                            ClientResponse.Headers clientHeaders = clientResponse.headers();

                            Optional<MediaType> contentType = clientHeaders.contentType();
                            if (contentType.isPresent()) {
                                builder = builder.contentType(contentType.get());
                            }

                            OptionalLong contentLength = clientHeaders.contentLength();
                            if (contentLength.isPresent()) {
                                builder = builder.contentLength(contentLength.getAsLong());
                            }

                            return builder.body(BodyInserters.fromDataBuffers(clientResponse.bodyToFlux(DataBuffer.class)));
                        }
                );
    }

    private Map<String, List<String>> getRequestMetadata() {
        try {
            return credentials.getRequestMetadata(null);
        } catch (IOException e) {
            return new HashMap<>();
        }
    }
}
