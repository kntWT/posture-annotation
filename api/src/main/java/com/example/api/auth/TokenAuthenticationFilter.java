package com.example.api.auth;

import java.util.ArrayList;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import com.example.api.services.UserService;
import com.generated.model.User;

public class TokenAuthenticationFilter extends OncePerRequestFilter {

    @Autowired UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String headerAuth = request.getHeader("Authorization");
        System.err.println(headerAuth);
        if (headerAuth == null || !headerAuth.startsWith("Bearer ")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid Authorization header");
            return;
        }
        String token = headerAuth.substring(7);
        User user = userService.getUserByToken(token);
        if (user == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid Authorization header");
            return;
        }

        String username = user.getName();
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>()));
    }
}
