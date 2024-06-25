package ru.netology.cloud_server.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ServerException extends RuntimeException {
    public ServerException(String message) {
        super(message);
    }
}
