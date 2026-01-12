package com.mianki.servicio.servicepart.repositories;

import com.mianki.servicio.servicepart.models.entities.MyNotes;
import com.mianki.servicio.servicepart.models.enums.NoteLevel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MyNotesRepository extends CrudRepository<MyNotes, Long> {
    @Query("select p from MyNotes p where p.nextreview < :datevalue and p.user.username=:username order by p.id")
    List<MyNotes> findByAfterNextreview(@Param("username") String username, @Param("datevalue") LocalDateTime datevalue, Pageable pageable);

    @Query("select p from MyNotes p where p.user.username = :username and p.id = :id")
    Optional<MyNotes> findByIdAndUsername(@Param("id") Long id, @Param("username") String username);

    @Query("select count(p) from MyNotes p where p.user.username = :username and p.noteLevel=:notelevel and p.nextreview < :datevalue")
    Integer countByUsernameAndNotelevel(@Param("username") String username, @Param("notelevel") NoteLevel notelevel, @Param("datevalue") LocalDateTime datevalue);

}
