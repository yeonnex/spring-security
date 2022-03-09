package com.example.userservice.repo;

import com.example.userservice.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name); // ROLE_USER, ROLE-MANAGER, ROLE_ADMIN
}
