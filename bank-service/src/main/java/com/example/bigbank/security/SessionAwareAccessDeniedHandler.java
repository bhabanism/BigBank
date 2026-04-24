package com.example.bigbank.security;

import java.io.IOException;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Maps access-denied to the right HTTP status for a stateless JWT API:
 * <ul>
 *   <li><b>401</b> — no real principal (anonymous, missing, or invalid session for this request). E.g. expired JWT
 *     that left the context without a proper authentication.</li>
 *   <li><b>403</b> — authenticated user, but not allowed (e.g. missing role for {@code @PreAuthorize}).</li>
 * </ul>
 */
@Component
public class SessionAwareAccessDeniedHandler implements AccessDeniedHandler {

    private final AuthenticationTrustResolver trustResolver = new AuthenticationTrustResolverImpl();

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException, ServletException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null
                || trustResolver.isAnonymous(auth)
                || !auth.isAuthenticated()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        response.sendError(HttpServletResponse.SC_FORBIDDEN);
    }
}
