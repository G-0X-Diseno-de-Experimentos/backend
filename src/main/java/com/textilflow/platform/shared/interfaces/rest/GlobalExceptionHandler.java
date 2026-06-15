package com.textilflow.platform.shared.interfaces.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException exception) {
        Map<String, String> fields = new LinkedHashMap<>();
        exception.getBindingResult().getFieldErrors()
                .forEach(error -> fields.put(error.getField(), error.getDefaultMessage()));

        return ResponseEntity.badRequest().body(errorBody(
                HttpStatus.BAD_REQUEST,
                "Validation failed",
                fields
        ));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntime(RuntimeException exception) {
        String message = exception.getMessage() == null || exception.getMessage().isBlank()
                ? "Unexpected server error"
                : exception.getMessage();

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String normalizedMessage = message.toLowerCase();

        if (normalizedMessage.contains("invalid password") || normalizedMessage.contains("invalid reset token")) {
            status = HttpStatus.UNAUTHORIZED;
        } else if (normalizedMessage.contains("not found")) {
            status = HttpStatus.NOT_FOUND;
        } else if (normalizedMessage.contains("already exists")) {
            status = HttpStatus.CONFLICT;
        } else if (normalizedMessage.contains("invalid") || normalizedMessage.contains("required")) {
            status = HttpStatus.BAD_REQUEST;
        }

        return ResponseEntity.status(status).body(errorBody(status, message, null));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorBody(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected server error", null));
    }

    private Map<String, Object> errorBody(HttpStatus status, String message, Map<String, String> fields) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        if (fields != null && !fields.isEmpty()) {
            body.put("fields", fields);
        }
        return body;
    }
}
