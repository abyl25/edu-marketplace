package com.seniorproject.educationplatform.services;

import com.seniorproject.educationplatform.dto.AddCourseDto;
import com.seniorproject.educationplatform.models.Course;
import com.seniorproject.educationplatform.models.Level;
import com.seniorproject.educationplatform.models.Topic;
import com.seniorproject.educationplatform.models.User;
import com.seniorproject.educationplatform.repositories.CourseRepo;
import com.seniorproject.educationplatform.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CourseService {
    private CourseRepo courseRepo;
    private UserRepo userRepo;

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

    public Course addCourse(AddCourseDto addCourseDto) {
        Course course = courseDtoToEntity(addCourseDto);
        Date date = new Date(System.currentTimeMillis());
        course.setAddedDate(date);
        course.setLastUpdate(date);

        String permaLink = createPermaLink(course.getTitle());
        course.setPermaLink(permaLink);

        courseRepo.save(course);
        return course;
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

    private String createPermaLink(String name) {
        String permaLink = name.toLowerCase();
        permaLink = permaLink.replace(" ", "-");
        return permaLink;
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

    private Course courseDtoToEntity(AddCourseDto addCourseDto) {
        Course course = new Course();
        course.setTitle(addCourseDto.getTitle());
        course.setSubtitle(addCourseDto.getSubtitle());
        User instructor = userRepo.findById(addCourseDto.getInstructorId()).orElse(null);
        course.setInstructor(instructor);
        course.setDescription(addCourseDto.getDescription());
        Level level = Level.valueOf(addCourseDto.getLevel());
        course.setLevel(level);
        course.setLanguage(addCourseDto.getLanguage());
        course.setCaption(addCourseDto.getCaption());
        course.setPrice(addCourseDto.getPrice());
        course.setImage("");
        return course;
    }

}
