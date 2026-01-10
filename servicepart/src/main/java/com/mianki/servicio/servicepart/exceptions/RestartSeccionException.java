package com.mianki.servicio.servicepart.exceptions;

public class RestartSeccionException extends RuntimeException{
    public RestartSeccionException() {
        super("Vuelva a iniciar session");
    }

    public RestartSeccionException(String message) {
        super(message);
    }
}
