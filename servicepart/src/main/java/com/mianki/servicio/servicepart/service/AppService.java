package com.mianki.servicio.servicepart.service;

import com.mianki.servicio.servicepart.models.dtos.CountNotes;
import com.mianki.servicio.servicepart.models.dtos.MyNotesDto;
import com.mianki.servicio.servicepart.models.dtos.OptionRequest;
import com.mianki.servicio.servicepart.models.entities.MyNotes;

import java.util.List;

public interface AppService {

    MyNotes update(OptionRequest optionRequest);
    MyNotes save(MyNotesDto myNotesDto);
    void resetnote(Long id);
    void deletenote(Long id);
    CountNotes findNotecount();
}
