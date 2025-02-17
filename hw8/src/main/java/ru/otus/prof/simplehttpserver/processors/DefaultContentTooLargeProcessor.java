package ru.otus.prof.simplehttpserver.processors;

import ru.otus.prof.simplehttpserver.HttpRequest;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class DefaultContentTooLargeProcessor implements RequestProcessor{
    @Override
    public void execute(HttpRequest request, OutputStream out) throws IOException {
        String response = "" +
                "HTTP/1.1 413 OK\r\n" +
                "Content-Type: text/html\r\n" +
                "\r\n" +
                "<html><body><h1>413 Content Too Large</h1></body></html>";
        out.write(response.getBytes(StandardCharsets.UTF_8));
    }
}
