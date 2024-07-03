package com.example.backend.repositories;

import com.example.backend.models.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Boolean existsByName(String name);
}
