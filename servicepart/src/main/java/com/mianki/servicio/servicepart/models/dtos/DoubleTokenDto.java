package com.mianki.servicio.servicepart.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DoubleTokenDto {
    private String accessToken;
    private String loginToken;
}
