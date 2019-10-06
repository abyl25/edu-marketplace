package com.seniorproject.educationplatform.repositories;

import com.seniorproject.educationplatform.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
    boolean existsByUserName(String username);

    User findByUserName(String userName);

    User findByEmail(String email);
}
