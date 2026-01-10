package com.mianki.servicio.servicepart.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginTokenClaims {
    private Long id;
    private Long idlogin;
    private String username;
}
