package com.mianki.servicio.servicepart.repositories;


import com.mianki.servicio.servicepart.models.entities.LoginEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LoginRepository extends CrudRepository<LoginEntity, Long> {
    @Query("select p from LoginEntity p where p.id = :id and p.user.username=:username")
    Optional<LoginEntity> findByUsernameAndId(@Param("username") String username, @Param("id") Long id);
}
