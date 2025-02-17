package ru.otus.prof.simplehttpserver.processors;

import ru.otus.prof.simplehttpserver.HttpRequest;
import ru.otus.prof.simplehttpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class ShutDownProcessor implements RequestProcessor {
    private HttpServer httpServer;

    public ShutDownProcessor(HttpServer httpServer) {
        this.httpServer = httpServer;
    }

    @Override
    public void execute(HttpRequest request, OutputStream out) throws IOException {
        String response = "" +
                "HTTP/1.1 200 OK\r\n" +
                "Content-Type: text/html\r\n" +
                "\r\n" +
                "<html><body><h1>Shutting down server</h1></body></html>";
        out.write(response.getBytes(StandardCharsets.UTF_8));
        httpServer.shutDown();
    }
}
