package com.seniorproject.educationplatform.repositories;

import com.seniorproject.educationplatform.models.Course;
import com.seniorproject.educationplatform.models.CourseStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseRepo extends JpaRepository<Course, Long> {
    boolean existsByTitleIgnoreCase(String title);

    Course findByTitleIgnoreCase(String title);

    boolean existsByPermaLink(String permaLink);

    Course findByPermaLink(String permaLink);

    List<Course> findByStatus(CourseStatus status);

    List<Course> findByCategoryNameIgnoreCase(String categoryName);

    List<Course> findByTopicId(Long topicId);

    List<Course> findByTopicNameIgnoreCase(String topicName);

    List<Course> findByInstructorId(Long id);

    Course findByIdAndInstructorId(Long id, Long instructor_id);

//    @Query(value = "SELECT c.name FROM Course c WHERE name=:name1", nativeQuery = true)
//    Course fetchByName(@Param("name1") String name);
}
