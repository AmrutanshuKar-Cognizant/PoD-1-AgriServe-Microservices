package com.cognizant.agriserve.userservice.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
public class InternalServiceAuthFilter extends OncePerRequestFilter {

    private String internalServiceKey;

    public InternalServiceAuthFilter(String internalServiceKey) {
        this.internalServiceKey = internalServiceKey;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String headerKey = request.getHeader("X-Internal-service-key");

        // Only process if the header actually exists in the request
        if (headerKey != null) {
            log.info("Received Internal Service Key from Feign Client: {}", headerKey);

            if (headerKey.equals(internalServiceKey)) {
                log.info("Key matched! Granting ROLE_SERVICE to this request.");

                // Construct the Spring Security Token
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        "INTERNAL_SERVICE",
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_SERVICE")) // Must exactly match the Controller!
                );

                // Inject it into the Security Context
                SecurityContextHolder.getContext().setAuthentication(auth);
            } else {
                log.warn("Key MISMATCH! Expected: {} but got: {}", internalServiceKey, headerKey);
            }
        }

        filterChain.doFilter(request, response);
    }
}