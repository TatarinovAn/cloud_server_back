package ru.netology.cloud_server.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import ru.netology.cloud_server.dto.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.netology.cloud_server.exception.InputDataException;
import ru.netology.cloud_server.exception.ServerException;

import java.util.concurrent.atomic.AtomicInteger;

@ControllerAdvice
public class ExceptionHandlerAdvice {
    private final AtomicInteger id = new AtomicInteger(0);

    @ExceptionHandler(InputDataException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(InputDataException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(400, e.getMessage()));
    }

    @ExceptionHandler(ServerException.class)
    public ResponseEntity<ErrorResponse> handleInternalServerError(ServerException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(500, e.getMessage()));
    }

}
