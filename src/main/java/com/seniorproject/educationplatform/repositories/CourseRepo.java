package com.seniorproject.educationplatform.repositories;

import com.seniorproject.educationplatform.models.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepo extends JpaRepository<Course, Long> {
    Course findByTitle(String title);

    Course findByPermaLink(String permaLink);

    List<Course> findByCategoryName(String categoryName);

    List<Course> findByTopicId(Long topicId);

    List<Course> findByTopicName(String topicName);

    List<Course> findByInstructorId(Long id);

}
