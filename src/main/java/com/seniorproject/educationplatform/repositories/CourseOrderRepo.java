package com.seniorproject.educationplatform.repositories;

import com.seniorproject.educationplatform.models.CourseOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseOrderRepo extends JpaRepository<CourseOrder, Long> {
    List<CourseOrder> findByCourseId(Long courseId);

    List<CourseOrder> findByStudentId(Long studentId);

    boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);

    Long removeByStudentIdAndCourseId(Long studentId, Long courseId);
}
