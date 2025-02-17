package ru.otus.prof.simplehttpserver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.otus.prof.simplehttpserver.processors.*;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class Dispatcher {
    private static final Logger logger = LogManager.getLogger(Dispatcher.class);

    private final Map<String, RequestProcessor> processors;

    private final RequestProcessor defaultInternalServerErrorRequestProcessor;
    private final RequestProcessor defaultNotFoundRequestProcessor;

    public Dispatcher(HttpServer httpServer) {
        this.processors = new HashMap<>();
        this.processors.put("GET /", new DefaultGetProcessor());
        this.processors.put("GET /test", new DefaultGetProcessor());
        this.processors.put("GET /shutdown", new ShutDownProcessor(httpServer));

        this.defaultInternalServerErrorRequestProcessor = new DefaultInternalServerErrorRequestProcessor();
        this.defaultNotFoundRequestProcessor = new DefaultNotFoundRequestProcessor();
    }

    public void execute(HttpRequest request, OutputStream out) throws IOException {
        logger.info("Handle request: {} {}", request.getMethod(), request.getUri());
        try {
            if (!processors.containsKey(request.getRoutingKey())) {
                logger.warn("routing not found for: {}", request.getRoutingKey());
                defaultNotFoundRequestProcessor.execute(request, out);
                return;
            }
            logger.info("routing for: {}", request.getRoutingKey());
            processors.get(request.getRoutingKey()).execute(request, out);

        } catch (IOException e) {
            logger.error("Error on handling request: ", e);
            defaultInternalServerErrorRequestProcessor.execute(request, out);
        }
    }
}