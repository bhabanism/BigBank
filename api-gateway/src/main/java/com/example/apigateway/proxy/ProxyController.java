package com.example.apigateway.proxy;

import java.net.URI;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class ProxyController {

    private final RouteResolver routeResolver;

    public ProxyController(RouteResolver routeResolver) {
        this.routeResolver = routeResolver;
    }

    @RequestMapping("/api/**")
    public ResponseEntity<byte[]> proxy(
            HttpMethod method,
            @RequestBody(required = false) byte[] body,
            @RequestHeader HttpHeaders incomingHeaders,
            HttpServletRequest servletRequest) {

        String requestPath = servletRequest.getRequestURI();
        String targetBaseUrl = routeResolver.resolveTargetBase(requestPath);
        if (targetBaseUrl == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No gateway route defined for path");
        }

        String query = servletRequest.getQueryString();
        String targetUrl = targetBaseUrl + requestPath + (query == null ? "" : "?" + query);

        WebClient.RequestBodySpec requestSpec = WebClient.builder().build()
                .method(method)
                .uri(URI.create(targetUrl));

        HttpHeaders outgoing = new HttpHeaders();
        incomingHeaders.forEach((k, v) -> {
            if (!"host".equalsIgnoreCase(k) && !"content-length".equalsIgnoreCase(k)) {
                outgoing.put(k, v);
            }
        });
        requestSpec.headers(h -> h.addAll(outgoing));

        WebClient.RequestHeadersSpec<?> headersSpec = body != null ? requestSpec.bodyValue(body) : requestSpec;

        WebClient.ResponseSpec responseSpec = headersSpec.retrieve();
        return responseSpec.toEntity(byte[].class).block();
    }
}
