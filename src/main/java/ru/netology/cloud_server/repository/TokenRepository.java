package ru.netology.cloud_server.repository;

import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class TokenRepository {
    //потокобезопасная коллекция
    private final Map<String, String> authTokenMap = new ConcurrentHashMap<>();


    // добавить токен
    public void putAuthToken(String username, String authToken) {
        authTokenMap.put(username, authToken);
    }

    // удалить токен
    public void removeAuthTokenByUsername(String username) {
        authTokenMap.remove(username);
    }

    // получить токен пользователя
    public Optional<String> getAuthTokenByUsername(String username) {
        return Optional.ofNullable(authTokenMap.get(username));
    }

}
