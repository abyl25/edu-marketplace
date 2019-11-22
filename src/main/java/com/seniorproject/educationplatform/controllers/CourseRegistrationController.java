package com.seniorproject.educationplatform.controllers;

import com.seniorproject.educationplatform.dto.course.CourseOrderReqDto;
import com.seniorproject.educationplatform.dto.course.InstructorCourseStudents;
import com.seniorproject.educationplatform.models.Course;
import com.seniorproject.educationplatform.services.CourseOrderService;
import com.seniorproject.educationplatform.services.CourseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CourseRegistrationController {
    private CourseOrderService courseOrderService;
    private CourseService courseService;

    public CourseRegistrationController(CourseOrderService courseOrderService) {
        this.courseOrderService = courseOrderService;
    }

    @GetMapping("/user/{userId}/courses")
    public ResponseEntity getStudentRegisteredCourses(@PathVariable Long userId) {
        List<Course> courses = courseOrderService.getRegisteredCourses(userId);
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/user/{userId}/courses/{courseId}")
    public ResponseEntity getStudentRegisteredCourseById(@PathVariable Long userId, @PathVariable Long courseId) {
        return courseOrderService.getRegisteredCourse(userId, courseId);
    }

    @PostMapping("/courses/register")
    public ResponseEntity registerToCourse(@RequestBody CourseOrderReqDto courseOrderReqDto) {
        System.out.println("LOG: courseOrderReqDto: " + courseOrderReqDto);
        return courseOrderService.purchaseCourse(courseOrderReqDto);
    }

    @PostMapping("/user/{userId}/courses/{courseId}/drop")
    public ResponseEntity dropFromCourse(@PathVariable Long userId, @PathVariable Long courseId) {
        courseOrderService.dropFromCourse(userId, courseId);
        return ResponseEntity.ok("Dropped from course!");
    }

    @GetMapping("/instructor/{instructorId}/courses/{courseId}/students")
    public ResponseEntity getInstructorStudents(@PathVariable Long instructorId, @PathVariable Long courseId) {
        return courseOrderService.getInstructorStudentsByCourseId(instructorId, courseId);
    }

//    @GetMapping("/instructor/{instructorId}/courses/{courseId}/students_jpql")
//    public ResponseEntity getInstructorStudentsJpql(@PathVariable Long instructorId, @PathVariable Long courseId) {
//        List students = courseOrderService.getInstructorStudentsJpql(courseId);
//        return ResponseEntity.ok(students);
//    }

}
