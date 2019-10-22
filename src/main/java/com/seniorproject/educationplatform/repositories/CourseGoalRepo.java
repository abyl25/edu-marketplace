package com.seniorproject.educationplatform.repositories;

import com.seniorproject.educationplatform.models.CourseGoal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseGoalRepo extends JpaRepository<CourseGoal, Long> {
    List<CourseGoal> findByCourseId(Long courseId);
}
