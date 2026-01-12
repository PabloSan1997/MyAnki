package com.mianki.servicio.servicepart.service;

import com.mianki.servicio.servicepart.models.dtos.*;

public interface UserService {
    DoubleTokenDto login(LoginDto loginDto);
    UserInfo userInfo();
    DoubleTokenDto register(RegisterDto registerDto);
    void logout(String token);
    TokenDto refreshToken(String token);
}
