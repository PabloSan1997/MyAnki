package com.mianki.servicio.servicepart.controllers;

import com.mianki.servicio.servicepart.exceptions.MyBadRequestException;
import com.mianki.servicio.servicepart.exceptions.RefreshAccessException;
import com.mianki.servicio.servicepart.exceptions.RestartSeccionException;
import com.mianki.servicio.servicepart.models.dtos.ErroDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler({
            NoResourceFoundException.class
    })
    public ResponseEntity<?> notfound(Exception e) {
        ErroDto erroDto = new ErroDto(HttpStatus.NOT_FOUND, e.getMessage());
        return ResponseEntity.status(erroDto.getStatusCode()).body(erroDto);
    }

    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            MyBadRequestException.class,
            MethodArgumentTypeMismatchException.class
    })
    public ResponseEntity<?> badrequest(Exception e) {
        ErroDto erroDto = new ErroDto(HttpStatus.BAD_REQUEST, e.getMessage());
        if (e instanceof MethodArgumentNotValidException err) {
            StringBuilder stringBuilder = new StringBuilder();
            for (FieldError fieldError : err.getFieldErrors()) {
                stringBuilder.append(fieldError.getField()).append(": ")
                        .append(fieldError.getDefaultMessage()).append(". ");
            }
            String message = stringBuilder.toString().trim();
            erroDto.setMessage(message);
        }
        else if(e instanceof MethodArgumentTypeMismatchException){
            erroDto.setMessage("Id no se puede convertir a valor 'Long'");
        }
        return ResponseEntity.status(erroDto.getStatusCode()).body(erroDto);
    }

    @ExceptionHandler({
            RestartSeccionException.class,
            RefreshAccessException.class
    })
    public ResponseEntity<?> unauthorized(Exception e) {
        ErroDto erroDto = new ErroDto(HttpStatus.UNAUTHORIZED, e.getMessage());
        if (e instanceof RestartSeccionException) {
            ResponseCookie cookie = ResponseCookie.from("milog", "")
                    .maxAge(0)
                    .path("/")
                    .httpOnly(true)
                    .sameSite("Lax")
                    .build();
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.SET_COOKIE, cookie.toString());
            return ResponseEntity.status(erroDto.getStatusCode()).headers(headers).body(erroDto);
        }
        return ResponseEntity.status(erroDto.getStatusCode()).body(erroDto);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> internalServer(Exception e){
        ErroDto erroDto = new ErroDto(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        return ResponseEntity.status(erroDto.getStatusCode()).body(erroDto);
    }
}
