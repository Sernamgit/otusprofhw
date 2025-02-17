package ru.otus.prof.simplehttpserver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class App {
    private static final Logger logger = LogManager.getLogger(App.class);

    public static void main(String[] args) {
        Properties prop = new Properties();
        int serverPort;
        int serverPoolSize;

        try (FileInputStream fis = new FileInputStream("hw8/src/main/resources/config.properties")) {
            prop.load(fis);

            serverPort = Integer.parseInt(prop.getProperty("port"));
            serverPoolSize = Integer.parseInt(prop.getProperty("pool.size"));

        } catch (IOException e) {
            logger.error("Couldn't load config");
            throw new RuntimeException(e);
        }

        new HttpServer(serverPort, serverPoolSize).start();

    }
}
