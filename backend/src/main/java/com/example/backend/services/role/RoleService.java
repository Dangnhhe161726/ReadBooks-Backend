package com.example.backend.services.role;

import com.example.backend.models.entities.Role;
import com.example.backend.repositories.RoleRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService implements IRoleService {
    private final RoleRepository roleRepository;

    @PostConstruct
    @Transactional
    public void initData() {
        createRoles("READER");
        createRoles("ADMIN");
    }

    public void createRoles(String name) {
        if (!roleRepository.existsByName(name)) {
            Role role = new Role();
            role.setName(name);
            roleRepository.save(role);
        }
    }
}
