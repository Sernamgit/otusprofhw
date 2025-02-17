package ru.otus.prof.simplehttpserver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServer {
    private static final Logger logger = LogManager.getLogger(HttpServer.class);
    private final int port;
    private final ExecutorService executorService;
    private final HttpRequestParser requestParser;
    private final Dispatcher dispatcher;
    private boolean isRunning;
    private ServerSocket serverSocket;

    public HttpServer(int port, int poolSize) {
        this.port = port;
        this.isRunning = true;
        this.dispatcher = new Dispatcher(this);
        this.requestParser = new HttpRequestParser();
        this.executorService = Executors.newFixedThreadPool(poolSize);

    }

    public void start() {
        try {
            serverSocket = new ServerSocket(port);
            logger.info("Server start on port: {}", port);
            while (isRunning) {
                Socket socket = serverSocket.accept();
                executorService.submit(() -> handleRequest(socket));
            }
        } catch (IOException e) {
            logger.error("http server error: ", e);
        } finally {
            executorService.shutdown();
        }
    }

    private void handleRequest(Socket socket) {
        try (socket) {
            HttpRequest request = requestParser.parse(socket);
            dispatcher.execute(request, socket.getOutputStream());
        } catch (IOException e) {
            logger.error("Error processing request: ", e);
        }
    }


    public void shutDown() {
        isRunning = false;
        shutdownServer();
    }

    private void shutdownServer() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            executorService.shutdown();
            logger.info("Server shut down.");
        } catch (IOException e) {
            logger.error("Error shutting down server: ", e);
        }
    }
}
