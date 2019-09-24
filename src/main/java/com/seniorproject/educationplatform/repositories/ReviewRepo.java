package com.seniorproject.educationplatform.repositories;

import com.seniorproject.educationplatform.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepo extends JpaRepository<Review, Long> {

}
