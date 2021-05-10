package com.justiniano.api.security.repository;

import com.justiniano.api.security.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolRepository extends JpaRepository<Rol, Integer> {
    Rol findByName(String name);
}
