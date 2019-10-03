package com.seniorproject.educationplatform.controllers;

import com.seniorproject.educationplatform.dto.AddCourseDto;
import com.seniorproject.educationplatform.models.Course;
import com.seniorproject.educationplatform.services.CourseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/courses")
@Slf4j
public class CourseController {
    private final CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public List<Course> getCourses() {
//        Pageable firstPage = PageRequest.of(0, 20); // Page<Course> // courseRepo.findAll(firstPage);
        return courseService.getCourses();
    }

    @GetMapping("/{courseId}")
    public Course getCourseById(@PathVariable Long courseId) {
        Optional<Course> optionalCourse = courseService.getCourseById(courseId);
        Course course = null;
        if (optionalCourse.isPresent()) {
            course = optionalCourse.get();
        }
        return course;
    }

    @PostMapping
    public Course addCourse(@RequestBody AddCourseDto course) {
        return courseService.addCourse(course);
    }

    @GetMapping("/link/{permaLink}")
    public Course getCourseByPermaLink(@PathVariable String permaLink) {
        return courseService.getCourseByPermalink(permaLink);
    }

    @GetMapping("/category/{categoryName}")
    public List<Course> getCoursesByCategoryName(@PathVariable String categoryName) throws Exception {
        return courseService.getAllCoursesByRootCategory(categoryName);
    }

    @GetMapping("/category/{categoryName}/{subCategoryName}")
    public List<Course> getCoursesBySubCategoryName(@PathVariable String categoryName, @PathVariable String subCategoryName) throws Exception {
        return courseService.getCoursesByCategory(categoryName, subCategoryName);
    }

    @GetMapping("/topic/{topicName}")
    public List<Course> getCoursesByTopicName(@PathVariable String topicName) {
        return courseService.getCoursesByTopic(topicName);
    }


}
