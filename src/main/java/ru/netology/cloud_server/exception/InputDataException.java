package ru.netology.cloud_server.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class InputDataException extends RuntimeException {
    public InputDataException(String message) {
        super(message);
    }
}
