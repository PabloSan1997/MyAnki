package com.mianki.servicio.servicepart.service;


import com.mianki.servicio.servicepart.models.dtos.LoginTokenClaims;
import com.mianki.servicio.servicepart.models.entities.UserEntity;

public interface JwtLoginService {
    LoginTokenClaims loginvalidation(String token);
    String loginToken(UserEntity user);
    void logout(LoginTokenClaims loginTokenClaims);
}
