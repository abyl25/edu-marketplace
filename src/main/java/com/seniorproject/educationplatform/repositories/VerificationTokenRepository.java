package com.seniorproject.educationplatform.repositories;

import com.seniorproject.educationplatform.models.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    VerificationToken findByToken(String token);
    VerificationToken findByUserEmail(String email);
}
