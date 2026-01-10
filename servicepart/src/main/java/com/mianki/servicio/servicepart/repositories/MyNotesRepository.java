package com.mianki.servicio.servicepart.repositories;

import com.mianki.servicio.servicepart.models.entities.MyNotes;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MyNotesRepository extends CrudRepository<MyNotes, Long> {
    @Query("select p from MyNotes p where p.nextreview < :datevalue and p.user.username=:username")
    List<MyNotes> findByAfterNextreview(@Param("username") String username, @Param("datevalue") LocalDateTime datevalue);

    @Query("select p from MyNotes p where p.user.username = :username and p.id = :id")
    Optional<MyNotes> findByIdAndUsername(@Param("id") Long id, @Param("username") String username);

}
