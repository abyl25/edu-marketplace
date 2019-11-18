package com.seniorproject.educationplatform.repositories;

import com.seniorproject.educationplatform.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo extends JpaRepository<Role, Long> {
    Role findByName(String name);

    Long removeByName(String roleName);
}
