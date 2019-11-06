package com.seniorproject.educationplatform.repositories;

import com.seniorproject.educationplatform.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepo extends JpaRepository<Review, Long> {
    List<Review> findByCourseId(Long courseId);

    int countByCourseId(Long courseId);
}
