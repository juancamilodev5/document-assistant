package com.docassistant.da_backend.common;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class InternalApiKeyFilter extends OncePerRequestFilter {

    @Value("${internal.api.key}")
    private String internalApiKey;

    private static final String INTERNAL_API_KEY_HEADER = "X-Internal-Api-Key";

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // only internal endpoints
        if (!request.getRequestURI().contains("/status")) {
            filterChain.doFilter(request, response);
            return;
        }

        String key = request.getHeader(INTERNAL_API_KEY_HEADER);

        if (key == null || !key.equals(internalApiKey)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("unauthorized");
            return;
        }

        filterChain.doFilter(request, response);
    }
}