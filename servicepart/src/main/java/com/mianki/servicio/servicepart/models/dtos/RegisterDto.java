package com.mianki.servicio.servicepart.models.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterDto {
    @Size(max = 60) @NotBlank
    private String username;
    @Size(max = 500) @NotBlank
    private String password;
    @Size(max = 60) @NotBlank
    private String nickname;
}
