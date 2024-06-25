package ru.netology.cloud_server.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.hibernate.ObjectNotFoundException;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.netology.cloud_server.dto.ErrorResponse;
import ru.netology.cloud_server.dto.LoginRequest;
import ru.netology.cloud_server.dto.LoginResponse;
import ru.netology.cloud_server.repository.TokenRepository;
import ru.netology.cloud_server.service.JwtService;
import ru.netology.cloud_server.utils.Logger;
import ru.netology.cloud_server.utils.UsernamePasswordAuthentication;


import java.io.IOException;

import static ru.netology.cloud_server.utils.Const.*;


@RequiredArgsConstructor
@Component
public class LoginAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    private final PasswordAuthentication passwordAuthentication;

    Logger log = Logger.getInstance();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException {
        if (request.getHeader(AUTH_TOKEN_KEY) == null) {
            var bodyJson = request.getReader().readLine();
            if (bodyJson != null) {
                var mapper = new ObjectMapper();
                var userDto = mapper.readValue(bodyJson, LoginRequest.class);
                var username = userDto.getLogin();
                var password = userDto.getPassword();

                try {
                    Authentication authentication = new UsernamePasswordAuthentication(username, password, null);
                    authentication = passwordAuthentication.auth(authentication);
                    var authToken = jwtService.generatedAuthToken(authentication);
                    System.out.println("authToken" + authToken);
                    tokenRepository.putAuthToken(username, authToken);

                    log.writeLog("Пользователь: " + username + " токен: " + authToken);

                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().write(mapper.writeValueAsString(new LoginResponse(authToken)));
                    response.getWriter().flush();
                } catch (BadCredentialsException | ObjectNotFoundException e) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write(mapper.writeValueAsString(
                            new ErrorResponse(HttpServletResponse.SC_BAD_REQUEST, e.getMessage())));
                    response.getWriter().flush();
                }
            }
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getServletPath().equals("/login");
    }
}