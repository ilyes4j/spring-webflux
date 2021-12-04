package com.github.ilyes4j.virtualapi.backend;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.web.server.adapter.WebHttpHandlerBuilder;
import reactor.netty.DisposableServer;
import reactor.netty.http.server.HttpServer;

public class BackendApplication {

    public static void main(String[] args) {

        ApplicationContext context = new AnnotationConfigApplicationContext(MockConfiguration.class);
        HttpHandler handler = WebHttpHandlerBuilder.applicationContext(context).build();
        ReactorHttpHandlerAdapter adapter = new ReactorHttpHandlerAdapter(handler);

        DisposableServer server = HttpServer.create()
                .host("0.0.0.0")
                .port(8080)
                .handle(adapter)
                .bindNow();

        server.onDispose().block();
    }
}
