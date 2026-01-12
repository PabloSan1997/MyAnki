package com.mianki.servicio.servicepart.models.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CountNotes {
    private Integer newnotes;
    private Integer goneovernotes;
    private Integer graduatednotes;
}
