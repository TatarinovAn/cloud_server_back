package ru.netology.cloud_server.authentication;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import ru.netology.cloud_server.repository.TokenRepository;

import ru.netology.cloud_server.service.JwtService;
import ru.netology.cloud_server.utils.Logger;

import java.io.IOException;
import java.util.Optional;


import static ru.netology.cloud_server.utils.Const.*;

@RequiredArgsConstructor
@Component
public class JwtSuccessCustomHandler extends
        HttpStatusReturningLogoutSuccessHandler implements LogoutSuccessHandler {



    @Autowired
    JwtService jwtService;



    @Autowired
    TokenRepository tokenRepository;


private Logger log = Logger.getInstance();

    @Override
    public void onLogoutSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication)
            throws IOException {

        var authorizationKey = request.getHeader(AUTH_TOKEN_KEY);
        if (Optional.ofNullable(authorizationKey).isPresent() && authorizationKey.startsWith(BEARER)) {
            authorizationKey = authorizationKey.replace(BEARER, "").trim();

            Claims claims = jwtService.getClaims(authorizationKey);
            var username = String.valueOf(claims.get(USERNAME));

            log.writeLog("Пользователь: " + username + " токен: " + authorizationKey);

            tokenRepository.removeAuthTokenByUsername(username);
        }

        response.setStatus(HttpServletResponse.SC_OK);
        super.onLogoutSuccess(request, response, authentication);
    }
}