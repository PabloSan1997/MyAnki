package com.mianki.servicio.servicepart.exceptions;

public class MyBadRequestException extends RuntimeException{
    public MyBadRequestException() {
        super("Vuelva a iniciar session");
    }

    public MyBadRequestException(String message) {
        super(message);
    }
}
