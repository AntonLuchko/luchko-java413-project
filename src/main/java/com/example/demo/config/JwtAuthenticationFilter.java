package com.example.demo.config;

import com.example.demo.entity.Users;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.JwtService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    public JwtAuthenticationFilter(JwtService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {

            String jwt = authHeader.substring(7);
            String email = jwtService.extractEmail(jwt);

            if (email == null) {
                throw new BadCredentialsException("Invalid token");
            }

            Users user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new BadCredentialsException("User not found"));

            // 403 — проверка блокировки
            if (user.getIsBlocked()) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is blocked by admin");
            }

            if (!user.isAccountNonLocked()) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User account is locked due to failed attempts");
            }


            if (!jwtService.isTokenValid(jwt, user)) {
                throw new BadCredentialsException("Invalid token");
            }

            var roles = jwtService.extractRoles(jwt).stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(user, null, roles);

            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authToken);
            filterChain.doFilter(request, response);

        } catch (BadCredentialsException | JwtException e) {
            SecurityContextHolder.clearContext();

            // Отдаём исключение EntryPoint-у
            request.setAttribute("exception", e);
            throw new BadCredentialsException(e.getMessage(), e);
        }
    }
}