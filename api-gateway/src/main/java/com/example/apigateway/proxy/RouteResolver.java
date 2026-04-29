package com.example.apigateway.proxy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RouteResolver {

    @Value("${gateway.routes.identity}")
    private String identityBaseUrl;

    @Value("${gateway.routes.banking}")
    private String bankingBaseUrl;

    public String resolveTargetBase(String path) {
        if (path.startsWith("/api/auth") || path.startsWith("/api/admin")) {
            return identityBaseUrl;
        }
        if (path.startsWith("/api/accounts") || path.startsWith("/api/customers")) {
            return bankingBaseUrl;
        }
        return null;
    }
}
