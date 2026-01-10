package com.mianki.servicio.servicepart.models.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {
    @Size(max = 60) @NotBlank
    private String username;
    @Size(max = 500) @NotBlank
    private String password;
}
