package ru.netology.cloud_server.service;

import io.jsonwebtoken.Jwts;

import io.jsonwebtoken.security.Keys;

import lombok.RequiredArgsConstructor;


import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Value;
import io.jsonwebtoken.Claims;
import ru.netology.cloud_server.repository.CloudRepositoryUsers;
import ru.netology.cloud_server.repository.TokenRepository;
import ru.netology.cloud_server.utils.Logger;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;


@RequiredArgsConstructor
@Service
public class JwtService {
    //ключ
    @Value("${token.signing.key}")
    private String jwtSigningKey;
    // срок годности токена
    @Value("${jwt.key.expiration}")
    private Long tokenExpiration;
    private final CloudRepositoryUsers cloudRepositoryUsers;
    private final Logger log = Logger.getInstance();

    private final TokenRepository tokenRepository;
    private SecretKey key;
    public static final String USERNAME = "username";

    // генерация ключа
    private SecretKey generatedSecretKey() {
        if (key == null) {
            key = Keys.hmacShaKeyFor(jwtSigningKey.getBytes(StandardCharsets.UTF_8));
        }
        //System.out.println("key in generationKey: " + key);
        return key;
    }

    //генерация токена
    public String generatedAuthToken(Authentication authentication) {
        String token = Jwts.builder()
                .setClaims(
                        Map.of(
                                USERNAME, authentication.getName()))
                .setExpiration(new Date(new Date().getTime() + tokenExpiration))
                .setSubject(authentication.getName())
                .signWith(generatedSecretKey())
                .compact();
        log.writeLog(STR."Token successfully generated for \{authentication.getName()}");
        // System.out.println("token in generatedAuthToken: " + token);
        return token;
    }

    //Утверждений JWT
    public Claims getClaims(String authToken) {

        // System.out.println("authToken in getClaims: " + authToken);
        return Jwts.parserBuilder()
                .setSigningKey(generatedSecretKey())
                .build()
                .parseClaimsJws(authToken)
                .getBody();
    }

    // Проверка токена
    public boolean isValidAuthToken(String authToken) {
        // System.out.println("authToken in isValidAuthToken: " + authToken);
        var claims = Jwts.parserBuilder()
                .setSigningKey(generatedSecretKey())
                .build()
                .parseClaimsJws(authToken)
                .getBody();

        var username = String.valueOf(claims.get(USERNAME));
        var user = cloudRepositoryUsers.findByUsername(username);
        var tokenFromMemory = tokenRepository.getAuthTokenByUsername(username);

        return claims.getExpiration().after(new Date())
                && user.isPresent()
                && tokenFromMemory.isPresent()
                && tokenFromMemory.get().equals(authToken);
    }

}
