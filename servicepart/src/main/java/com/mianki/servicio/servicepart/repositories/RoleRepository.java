package com.mianki.servicio.servicepart.repositories;


import com.mianki.servicio.servicepart.models.entities.RolesEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RoleRepository extends CrudRepository<RolesEntity, Long> {
    Optional<RolesEntity> findByName(String name);
}
