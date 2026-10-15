package org.mekkaoui.tp_jwt.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.mekkaoui.tp_jwt.service.CustomerUserDetailsService;
import org.mekkaoui.tp_jwt.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {
    private JwtService jwtService;
    private CustomerUserDetailsService customerUserDetailsService;
    @Autowired
    public JwtFilter(JwtService jwtService, CustomerUserDetailsService customerUserDetailsService) {
        this.jwtService = jwtService;
        this.customerUserDetailsService = customerUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response); return;
        }
        String token = authHeader.substring(7);
        String username = jwtService.extractUsername(token);
        if (username != null) {}


    }
}
