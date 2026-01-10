package com.mianki.servicio.servicepart.service;

import com.mianki.servicio.servicepart.models.dtos.WebSocketClaims;

public interface JwtSocketService {
    WebSocketClaims validationSocket(String token);
    String webSocketToken(WebSocketClaims webSocketClaims);
}
