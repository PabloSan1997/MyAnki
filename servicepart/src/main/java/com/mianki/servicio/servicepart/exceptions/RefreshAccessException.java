package com.mianki.servicio.servicepart.exceptions;

public class RefreshAccessException extends RuntimeException {
    public RefreshAccessException() {
        super("refresh");
    }

    public RefreshAccessException(String message) {
        super(message);
    }
}
