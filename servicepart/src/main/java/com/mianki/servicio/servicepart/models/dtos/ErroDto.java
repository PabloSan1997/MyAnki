package com.mianki.servicio.servicepart.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErroDto {
    private Integer statusCode;
    private String error;
    private String message;
    public ErroDto(HttpStatus status, String message){
        this.error = status.getReasonPhrase();
        this.statusCode = status.value();
        this.message = message;
    }

    public Date getTimestamp(){
        return new Date();
    }
}
