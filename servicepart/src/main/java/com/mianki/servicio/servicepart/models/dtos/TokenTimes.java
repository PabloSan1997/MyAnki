package com.mianki.servicio.servicepart.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenTimes {
    private long accesseconds;
    private long loginseconds;
    private long socketseconds;
}
