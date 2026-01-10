package com.mianki.servicio.servicepart.models.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MyNotesDto {
    @Size(max = 250)
    @NotBlank
    private String front;
    @Size(max = 250)
    private String back;
    @Size(max = 250)
    @NotBlank
    private String note;
}
