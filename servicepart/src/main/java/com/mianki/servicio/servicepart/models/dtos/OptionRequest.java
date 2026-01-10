package com.mianki.servicio.servicepart.models.dtos;

import com.mianki.servicio.servicepart.models.enums.OptionNote;
import lombok.Data;

@Data
public class OptionRequest {
    private Long idnote;
    private OptionNote option;
}
