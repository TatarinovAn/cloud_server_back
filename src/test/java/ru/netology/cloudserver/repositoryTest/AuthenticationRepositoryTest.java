package ru.netology.cloudserver.repositoryTest;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import ru.netology.cloud_server.repository.TokenRepository;
import ru.netology.cloud_server.service.JwtService;
import ru.netology.cloud_server.utils.UsernamePasswordAuthentication;
import ru.netology.cloudserver.CloudServerApplicationTests;

import static org.junit.jupiter.api.Assertions.*;

public class AuthenticationRepositoryTest extends CloudServerApplicationTests {


    TokenRepository tokenRepository = Mockito.mock(TokenRepository.class);
    JwtService jwtService = Mockito.mock(JwtService.class);


    @BeforeEach
    void init() {
        Authentication authentication = new UsernamePasswordAuthentication("USERNAME", null, null);
        var token = jwtService.generatedAuthToken(authentication);
        tokenRepository.putAuthToken("USERNAME", token);
    }

    @Test
    public void testToken() {
        var authToken = tokenRepository.getAuthTokenByUsername("USERNAME");
        assertNotNull(authToken);
    }
}
