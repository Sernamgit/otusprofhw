package ru.otus.prof.simplehttpserver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.otus.prof.simplehttpserver.processors.DefaultContentTooLargeProcessor;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestParser {
    private static final Logger logger = LogManager.getLogger(HttpRequestParser.class);
    private final int maxRequestSize = 5 * 1024 * 1024;

    public HttpRequest parse(Socket socket) throws IOException {
        HttpMethod method;
        String uri;
        Map<String, String> parameters = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        String body = "";

        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        logger.debug("reading method and uri");
        String line = reader.readLine();
        if (line == null) {
            return null;
        }

        String[] requestLine = line.split(" ");
        if (requestLine.length < 3) {
            return null;
        }

        method = HttpMethod.valueOf(requestLine[0]);
        uri = requestLine[1];

        logger.debug("reading params");
        if (uri.contains("?")) {
            String[] elements = uri.split("[?]");
            uri = elements[0];
            String[] keysValues = elements[1].split("&");
            for (String o : keysValues) {
                String[] keyValue = o.split("=");
                parameters.put(keyValue[0], keyValue[1]);
            }
        }

        logger.debug("reading headers");
        while ((line = reader.readLine()) != null && !line.isEmpty()) {
            String[] header = line.split(": ", 2);
            if (header.length == 2) {
                headers.put(header[0], header[1]);
            }
        }

        if (method == HttpMethod.POST){
            logger.debug("reading body");
            body = readBody(socket, headers);
        }


        return new HttpRequest(method, uri, parameters, headers, body);
    }


    private String readBody(Socket socket, Map<String, String> headers) throws IOException {
        int contentLength = Integer.parseInt(headers.getOrDefault("Content-Length", "0"));
        if (contentLength > maxRequestSize) {
            logger.error("Body to large, sending 413");
            new DefaultContentTooLargeProcessor().execute(null, socket.getOutputStream());
            return null;
        }

        ByteArrayOutputStream requestBuffer = new ByteArrayOutputStream();
        byte[] buffer = new byte[8192];
        int bytesRead;
        int totalRead = 0;

        while ((bytesRead = socket.getInputStream().read(buffer)) != - 1) {
            totalRead += bytesRead;
            if (totalRead > maxRequestSize) {
                logger.error("Body to large, sending 413");
                new DefaultContentTooLargeProcessor().execute(null, socket.getOutputStream());
                return null;
            }
            requestBuffer.write(buffer, 0, bytesRead);
        }
        return requestBuffer.toString();
    }
}
