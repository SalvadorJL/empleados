package com.mictlan.empleados.exception;

public class NoEmployeeException extends RuntimeException {
    public NoEmployeeException(String mensaje) {
        super(mensaje);
    }
}
