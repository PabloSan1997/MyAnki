package com.mianki.servicio.servicepart.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WebSocketClaims {
    private String username;
    private Long idlogin;
}
