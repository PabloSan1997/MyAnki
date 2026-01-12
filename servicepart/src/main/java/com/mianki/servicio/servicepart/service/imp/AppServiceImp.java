package com.mianki.servicio.servicepart.service.imp;

import com.mianki.servicio.servicepart.exceptions.MyBadRequestException;
import com.mianki.servicio.servicepart.exceptions.RestartSeccionException;
import com.mianki.servicio.servicepart.models.dtos.CountNotes;
import com.mianki.servicio.servicepart.models.dtos.MyNotesDto;
import com.mianki.servicio.servicepart.models.dtos.OptionRequest;
import com.mianki.servicio.servicepart.models.entities.MyNotes;
import com.mianki.servicio.servicepart.models.entities.UserEntity;
import com.mianki.servicio.servicepart.models.enums.NoteLevel;
import com.mianki.servicio.servicepart.repositories.MyNotesRepository;
import com.mianki.servicio.servicepart.repositories.UserRepository;
import com.mianki.servicio.servicepart.service.AppService;
import com.mianki.servicio.servicepart.service.utils.DaysCalculatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


@Service
@Transactional
public class AppServiceImp implements AppService {

    @Autowired
    private MyNotesRepository myNotesRepository;
    @Autowired
    private DaysCalculatorService daysCalculatorService;
    @Autowired
    private UserRepository userRepository;


    private UserEntity getUserAuthentication() {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findByUsername(username)
                .orElseThrow(RestartSeccionException::new);
    }


    @Override
    public List<MyNotes> findOne() {
        var user = getUserAuthentication();
        return myNotesRepository.findByAfterNextreview(user.getUsername(), LocalDateTime.now(), PageRequest.of(0, 2));
    }

    @Override
    public MyNotes update(OptionRequest optionRequest) {
        var user = getUserAuthentication();
        MyNotes notes = myNotesRepository.findByIdAndUsername(optionRequest.getIdnote(), user.getUsername())
                .orElseThrow(() -> new MyBadRequestException("id invalido"));
        return daysCalculatorService.calculateDays(notes, optionRequest.getOption());
    }

    @Override
    public MyNotes save(MyNotesDto myNotesDto) {
        var user = getUserAuthentication();
        var newnote = MyNotes.builder().note(myNotesDto.getNote())
                .back(myNotesDto.getBack())
                .user(user)
                .front(myNotesDto.getFront()).build();
        return myNotesRepository.save(newnote);
    }

    @Override
    public void resetnote(Long id) {
        var user = getUserAuthentication();
        var note = myNotesRepository.findByIdAndUsername(id, user.getUsername())
                .orElseThrow(() -> new MyBadRequestException("Id invalido"));

        note.setDays(0.0);
        note.setPreviewtimehard("1 min");
        note.setPreviewtimegood("1 min");
        note.setNoteLevel(NoteLevel.INITIAL);
        note.setNextreview(LocalDateTime.now());
    }

    @Override
    public void deletenote(Long id) {
        var user = getUserAuthentication();
        myNotesRepository.findByIdAndUsername(id, user.getUsername()).ifPresent(r -> {
            myNotesRepository.deleteById(r.getId());
        });
    }

    @Override
    public CountNotes findNotecount() {
        var user = getUserAuthentication();
        var localdate = LocalDateTime.now();
        int initialcount = myNotesRepository.countByUsernameAndNotelevel(user.getUsername(), NoteLevel.INITIAL, localdate);
        int nextcount = myNotesRepository.countByUsernameAndNotelevel(user.getUsername(), NoteLevel.NEXT, localdate);
        int pregraguated = myNotesRepository.countByUsernameAndNotelevel(user.getUsername(), NoteLevel.PREGRUADATED, localdate);
        int graduatedcount = myNotesRepository.countByUsernameAndNotelevel(user.getUsername(), NoteLevel.GRADUATED, localdate);

        return CountNotes.builder().newnotes(initialcount).goneovernotes(nextcount).graduatednotes(pregraguated + graduatedcount).build();
    }


}
