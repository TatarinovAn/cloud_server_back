package ru.netology.cloud_server.authentication;


import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import ru.netology.cloud_server.service.JwtService;
import ru.netology.cloud_server.utils.Logger;
import ru.netology.cloud_server.utils.UsernamePasswordAuthentication;


import java.io.IOException;
import java.util.Optional;


import static ru.netology.cloud_server.utils.Const.*;


@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {


    private final JwtService jwtService;

    public static Logger log = Logger.getInstance();


    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {


        var authorizationKey = request.getHeader(AUTH_TOKEN_KEY);
        log.writeLog("token: " + authorizationKey);

        if (Optional.ofNullable(authorizationKey).isPresent() && authorizationKey.startsWith(BEARER)) {
            authorizationKey = authorizationKey.replace(BEARER, "").trim();
            try {
                if (jwtService.isValidAuthToken(authorizationKey)) {
                    var claims = jwtService.getClaims(authorizationKey);
                    var username = String.valueOf(claims.get(USERNAME));
                    Authentication authentication = new UsernamePasswordAuthentication(username, null, null);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (JwtException e) {
                SecurityContextHolder.getContext().setAuthentication(null);
            }
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getServletPath().equals("/login");
    }
}
