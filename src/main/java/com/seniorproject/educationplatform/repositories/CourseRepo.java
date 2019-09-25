package com.seniorproject.educationplatform.repositories;

import com.seniorproject.educationplatform.models.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseRepo extends JpaRepository<Course, Long> {
    Course findByTitle(String title);

    Course findByPermaLink(String permaLink);

    List<Course> findByCategoryName(String categoryName);

    List<Course> findByTopicId(Long topicId);

    List<Course> findByTopicName(String topicName);

    List<Course> findByInstructorId(Long id);

//    @Query(value = "SELECT c.name FROM Course c WHERE name=:name1", nativeQuery = true)
//    Course fetchByName(@Param("name1") String name);
}
