package com.mictlan.empleados.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoEmployeeException.class)
    public ResponseEntity<Object> handlerNoEmpleadosException(NoEmployeeException ex) {
        Map<String, Object> cuerpo = new HashMap<>();
        cuerpo.put("timestamp", LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
        cuerpo.put("message", ex.getMessage());
        cuerpo.put("status", HttpStatus.NOT_FOUND.value());

        return new ResponseEntity<>(cuerpo, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Object> handlerInvalidException(BadRequestException ex) {
        Map<String, Object> cuerpo = new HashMap<>();
        cuerpo.put("timestamp", LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
        cuerpo.put("message", ex.getMessage());
        cuerpo.put("status", HttpStatus.BAD_REQUEST.value());

        return new ResponseEntity<>(cuerpo, HttpStatus.BAD_REQUEST);
    }
}
