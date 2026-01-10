package com.mianki.servicio.servicepart.service;

import com.mianki.servicio.servicepart.models.dtos.UserSecurityDto;

public interface JwtAccessService {
    UserSecurityDto accessValidation(String token);
    String accessToken(UserSecurityDto userSecurityDto);
}
