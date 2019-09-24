package com.seniorproject.educationplatform.services;

import com.seniorproject.educationplatform.models.Course;
import com.seniorproject.educationplatform.models.Topic;
import com.seniorproject.educationplatform.models.User;
import com.seniorproject.educationplatform.repositories.CourseRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {
    private CourseRepo courseRepo;

    @Autowired
    public CourseService(CourseRepo courseRepo) {
        this.courseRepo = courseRepo;
    }

    public List<Course> getCourses() {
        return courseRepo.findAll();
    }

    public Optional<Course> getCourseById(Long courseId) {
        return courseRepo.findById(courseId);
    }

    public Course addCourse(Course course) {
        courseRepo.save(course);
        return null;
    }

    public Course getCourseByTitle(String courseTitle) {
        return courseRepo.findByTitle(courseTitle);
    }

    public Course getCourseByPermalink (String permaLink) {
        return courseRepo.findByPermaLink(permaLink);
    }

    public List<Course> getCoursesByCategory(String categoryName) {
        return courseRepo.findByCategoryName(categoryName);
    }

    public List<Course> getCoursesByTopic(String topicName) {
        return courseRepo.findByTopicName(topicName);
    }

    public List<Course> getCoursesByInstructor(Long instructorId) {
        return courseRepo.findByInstructorId(instructorId);
    }

    public List<Course> getPopularCourses() {
        return null;
    }

    public List<Course> getTrendingCourses() {
        return null;
    }

    public List<Course> getBeginnerFavoriteCourses() {
        return null;
    }

    public List<Topic> getPopularTopics() {
        return null;
    }

    public List<User> getPopularInstructors() {
        return null;
    }

}
