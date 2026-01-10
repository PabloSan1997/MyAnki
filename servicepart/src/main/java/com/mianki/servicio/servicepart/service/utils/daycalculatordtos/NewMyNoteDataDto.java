package com.mianki.servicio.servicepart.service.utils.daycalculatordtos;

import com.mianki.servicio.servicepart.models.enums.NoteLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewMyNoteDataDto {
    private Double days;
    private NoteLevel noteLevel;
    private NoteLevel nextlevel;
}
