package ru.otus.prof.simplehttpserver.processors;

import ru.otus.prof.simplehttpserver.HttpRequest;

import java.io.IOException;
import java.io.OutputStream;

public interface RequestProcessor {
    void execute(HttpRequest request, OutputStream out) throws IOException;
}
