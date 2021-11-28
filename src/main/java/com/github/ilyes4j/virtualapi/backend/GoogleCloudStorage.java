package com.github.ilyes4j.virtualapi.backend;

import com.google.auth.Credentials;
import com.google.cloud.storage.StorageOptions;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class GoogleCloudStorage {

    private static final String CLOUD_STORAGE_URL = "https://storage.googleapis.com/";

    private final OkHttpClient client;
    private final String dlPartialUrl;
    private final Credentials credentials;

    public GoogleCloudStorage(String bucketId) throws IOException {
        StorageOptions options = StorageOptions.newBuilder().build();

        credentials = options.getScopedCredentials();
        credentials.getRequestMetadata(null);

        client = new OkHttpClient.Builder().build();
        dlPartialUrl = CLOUD_STORAGE_URL + bucketId;
    }

    public Mono<ServerResponse> fetchContent(String rawMetadata) {

        try {
            //noinspection BlockingMethodInNonBlockingContext
            Map<String, List<String>> headers = credentials.getRequestMetadata(null);

            Request.Builder requestBuilder = new Request.Builder();
            headers.forEach((key, value) -> requestBuilder.header(key, value.get(0)));
            Request request = requestBuilder.url(dlPartialUrl + rawMetadata).build();

            //noinspection BlockingMethodInNonBlockingContext
            Response response = client.newCall(request).execute();
            ResponseBody body = response.body();
            //noinspection BlockingMethodInNonBlockingContext
            byte[] data = body != null ? body.bytes() : null;

            //noinspection ConstantConditions
            return ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(data));
        } catch (IOException e) {
            return null;
        }
    }
}
