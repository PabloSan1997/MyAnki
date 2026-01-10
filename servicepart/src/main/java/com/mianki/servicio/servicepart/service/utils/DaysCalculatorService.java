package com.mianki.servicio.servicepart.service.utils;

import com.mianki.servicio.servicepart.models.entities.MyNotes;
import com.mianki.servicio.servicepart.models.enums.NoteLevel;
import com.mianki.servicio.servicepart.models.enums.OptionNote;
import com.mianki.servicio.servicepart.service.utils.daycalculatordtos.NewMyNoteDataDto;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class DaysCalculatorService {

    private final static Double hard = 1.5;
    private final static Double good = 2.5;

    public MyNotes calculateDays(MyNotes myNotes, OptionNote optionNote) {
        var res = calculatenewtime(myNotes.getDays(), myNotes.getNoteLevel(), optionNote);
        var resgood = calculatenewtime(res.getDays(), res.getNextlevel(), OptionNote.BIEN);
        var reshard = calculatenewtime(res.getDays(), res.getNextlevel(), OptionNote.DIFICIL);
        String timetestgood = writenexttime(resgood.getDays());
        String timetesthard = writenexttime(reshard.getDays());

        myNotes.setNoteLevel(res.getNoteLevel());
        myNotes.setDays(res.getDays());
        myNotes.setNextreview(myNotes.getNextreview().plusDays(res.getDays().longValue()));
        myNotes.setPreviewtimegood(timetestgood);
        myNotes.setPreviewtimehard(timetesthard);

        return myNotes;
    }

    private NewMyNoteDataDto calculatenewtime(Double days, NoteLevel level, OptionNote optionNote) {
        if (level.equals(NoteLevel.INITIAL)) {
            if (optionNote.equals(OptionNote.DIFICIL))
                return new NewMyNoteDataDto(0.0, NoteLevel.INITIAL, NoteLevel.NEXT);
            return new NewMyNoteDataDto(0.0, NoteLevel.NEXT, NoteLevel.PREGRUADATED);
        }else if(level.equals(NoteLevel.NEXT)){
            if (optionNote.equals(OptionNote.DIFICIL))
                return new NewMyNoteDataDto(0.0, NoteLevel.INITIAL, NoteLevel.NEXT);
            return new NewMyNoteDataDto(1.0, NoteLevel.PREGRUADATED, NoteLevel.GRADUATED);
        }
        else if (level.equals(NoteLevel.PREGRUADATED)) {
            return new NewMyNoteDataDto(1.0, NoteLevel.GRADUATED, NoteLevel.GRADUATED);
        } else if (level.equals(NoteLevel.GRADUATED)) {
            double value = optionNote.equals(OptionNote.BIEN) ? good : hard;
            return new NewMyNoteDataDto(value * days, NoteLevel.GRADUATED, NoteLevel.GRADUATED);
        }
        throw new RuntimeException("No se puedje procesar las tarjetas");
    }

    private String writenexttime(Double days) {
        LocalDateTime datenow = LocalDateTime.now();
        LocalDateTime finaldate = datenow.plusDays(days.longValue());
        if (days == 0)
            return "1 min";
        if (days < 15)
            return days + " dias";
        if (days < 31) {
            long weeks = ChronoUnit.WEEKS.between(
                    datenow,
                    finaldate
            );
            return weeks + " semanas";
        }
        if (days < 365) {
            long months = ChronoUnit.MONTHS.between(
                    datenow, finaldate
            );
            return months + " meses";
        }
        long years = ChronoUnit.YEARS.between(datenow, finaldate);
        return years + " años";
    }
}
