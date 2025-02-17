package ru.otus.prof.simplehttpserver;


import java.util.Map;

public class HttpRequest {
    private HttpMethod method;
    private String uri;
    private Map<String, String> parameters;
    private Map<String, String> headers;
    private String body;

    public HttpRequest(HttpMethod method, String uri, Map<String, String> parameters, Map<String, String> headers, String body) {
        this.method = method;
        this.uri = uri;
        this.parameters = parameters;
        this.headers = headers;
        this.body = body;
    }

    public String getRoutingKey() {
        return method + " " + uri;
    }

    public String getBody() {
        return body;
    }

    public String getUri() {
        return uri;
    }

    public HttpMethod getMethod() {
        return method;
    }

}
