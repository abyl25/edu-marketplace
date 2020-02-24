package com.seniorproject.educationplatform.controllers;

import com.seniorproject.educationplatform.dto.course.CourseOrderReqDto;
import com.seniorproject.educationplatform.dto.course.InstructorCourseStudents;
import com.seniorproject.educationplatform.dto.course.MultiCourseOrderReqDto;
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
    public ResponseEntity<Object> getStudentRegisteredCourses(@PathVariable Long userId) {
        List<Course> courses = courseOrderService.getRegisteredCourses(userId);
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/user/{userId}/courses/{courseId}")
    public ResponseEntity<Object> getStudentRegisteredCourseById(@PathVariable Long userId, @PathVariable Long courseId) {
        return courseOrderService.getRegisteredCourse(userId, courseId);
    }

    @PostMapping("/courses/register")
    public ResponseEntity<Object> registerToCourse(@RequestBody CourseOrderReqDto courseOrderReqDto) {
        System.out.println("LOG: courseOrderReqDto: " + courseOrderReqDto);
        return courseOrderService.purchaseCourse(courseOrderReqDto);
    }

    @PostMapping("/courses/multi-register")
    public ResponseEntity<Object> registerToMultipleCourses(@RequestBody MultiCourseOrderReqDto courseOrders) {
        System.out.println("LOG: MultiCourseOrderReqDto: " + courseOrders);
        return courseOrderService.registerToMultipleCourses(courseOrders);
//        return ResponseEntity.ok("Good");
    }

    @PostMapping("/user/{userId}/courses/{courseId}/drop")
    public ResponseEntity<Object> dropFromCourse(@PathVariable Long userId, @PathVariable Long courseId) {
        System.out.println("LOG: dropFromCourse, userId: " + userId + "; courseId: " + courseId);
        courseOrderService.dropFromCourse(userId, courseId);
        return ResponseEntity.ok("Dropped from course!");
    }

    @GetMapping("/instructor/{instructorId}/courses/{courseId}/students")
    public ResponseEntity<Object> getInstructorStudentsByCourseId(@PathVariable Long instructorId, @PathVariable Long courseId) {
        return courseOrderService.getInstructorStudentsByCourseId(instructorId, courseId);
    }

    @GetMapping("/instructor/{instructorId}/courses/students/count")
    public ResponseEntity<Object> getInstructorStudentsCount(@PathVariable Long instructorId) {
        int count = courseOrderService.getInstructorAllStudentsCount(instructorId);
        return ResponseEntity.ok(count);
    }

//    @GetMapping("/instructor/{instructorId}/courses/{courseId}/students_jpql")
//    public ResponseEntity getInstructorStudentsJpql(@PathVariable Long instructorId, @PathVariable Long courseId) {
//        List students = courseOrderService.getInstructorStudentsJpql(courseId);
//        return ResponseEntity.ok(students);
//    }

}
