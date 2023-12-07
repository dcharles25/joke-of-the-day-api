package com.interview.jotd;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import com.interview.jotd.api.error.handler.BaseHTTPErrorHandler;
import com.interview.jotd.api.error.handler.ConstraintViolationResponseHandler;
import com.interview.jotd.api.error.handler.UnknownErrorResponseHandler;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URI;


@Slf4j
public class Main {
    public static final String BASE_URI = "http://0.0.0.0:8080/api/";

    public static HttpServer startServer() {
        // create a resource config that scans for JAX-RS resources and providers
        final ResourceConfig rc = new ResourceConfig().packages("com.interview.jotd");

        rc.register(new ApplicationBinder());
        rc.register(BaseHTTPErrorHandler.class);
        rc.register(ConstraintViolationResponseHandler.class);
        rc.register(UnknownErrorResponseHandler.class);

        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    public static void main(String[] args) throws IOException {
        final HttpServer server = startServer();

        //Register shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                log.info("Servier stopping...");
                server.shutdown();
            }
        }, "shutdownHook"));

        try {
            server.start();
            log.info("Server starting. Press CTRL^C to exit.");
            Thread.currentThread().join();
        } catch (Exception e) {
            log.error(
                    "There was an error while starting Grizzly HTTP server.", e);
        }
    }
}

