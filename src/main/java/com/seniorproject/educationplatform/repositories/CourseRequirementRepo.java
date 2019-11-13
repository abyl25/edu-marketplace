package com.seniorproject.educationplatform.repositories;

import com.seniorproject.educationplatform.models.CourseRequirement;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRequirementRepo extends JpaRepository<CourseRequirement, Long> {
    List<CourseRequirement> findByCourseId(Long courseId);

    boolean existsByCourseIdAndName(Long courseId, String requirementName);

    Long removeByCourseIdAndName(Long courseId, String name);
}
