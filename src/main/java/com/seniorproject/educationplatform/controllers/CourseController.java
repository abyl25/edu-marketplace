package com.seniorproject.educationplatform.controllers;

import com.seniorproject.educationplatform.ESModels.ESCourse;
import com.seniorproject.educationplatform.dto.course.*;
import com.seniorproject.educationplatform.models.Course;
import com.seniorproject.educationplatform.services.CourseOrderService;
import com.seniorproject.educationplatform.services.CourseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api")
public class CourseController {
    private CourseService courseService;
    private CourseOrderService courseOrderService;

    @Autowired
    public CourseController(CourseService courseService, CourseOrderService courseOrderService) {
        this.courseService = courseService;
        this.courseOrderService = courseOrderService;
    }

    @GetMapping("/courses")
    public ResponseEntity getCourses() {
//        Pageable firstPage = PageRequest.of(0, 20); // Page<Course> // courseRepo.findAll(firstPage);
        List<Course> courses = courseService.getCourses();
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/courses/{courseId}")
    public ResponseEntity getCourseById(@PathVariable Long courseId) {
        Optional<Course> optionalCourse = courseService.getCourseById(courseId);
        if (optionalCourse.isPresent()) {
            Course course = optionalCourse.get();
            return ResponseEntity.ok(course);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/courses")
    public ResponseEntity createCourse(@Valid @RequestBody CreateCourseReq createCourseReq) {
        System.out.println("LOG: CreateCourseReq: " + createCourseReq);
        Course newCourse = courseService.createCourse(createCourseReq);
        return ResponseEntity.ok(newCourse);
    }

    @DeleteMapping("/courses/{courseId}")
    public ResponseEntity removeCourse(@PathVariable Long courseId) {
        courseService.removeCourse(courseId);
        return ResponseEntity.ok("Course removed");
    }

    @PutMapping("/courses/{courseId}")
    public ResponseEntity updateCourseMainInfo(@PathVariable Long courseId, @Valid @RequestBody CreateCourseReq updateCourse) {
        System.out.println("LOG: updateCourseReq: " + updateCourse);
        Course updatedCourse = courseService.updateCourseMainInfo(courseId, updateCourse);
        return ResponseEntity.ok(updatedCourse);
    }

    @GetMapping("/courses/{id}/target")
    public ResponseEntity getCourseTarget(@PathVariable Long id) {
        return courseService.getCourseTarget(id);
    }

    @RequestMapping(value = "/courses/reqs", method = RequestMethod.POST, consumes="application/json", produces = "application/json")
    public ResponseEntity addCourseReqs(@Valid @RequestBody AddCourseReq courseReq) {
        courseService.addCourseReqs(courseReq);
        return ResponseEntity.ok("Course requirements added");
    }

    @RequestMapping(value = "/courses/goals", method = RequestMethod.POST, consumes="application/json", produces = "application/json")
    public ResponseEntity addCourseGoals(@Valid @RequestBody AddCourseGoal courseGoal) {
        courseService.addCourseGoals(courseGoal);
        return ResponseEntity.ok("Course goals added");
    }

    @RequestMapping(value = "/courses/target", method = RequestMethod.POST, consumes="application/json", produces = "application/json")
    public ResponseEntity addCourseTarget(@Valid @RequestBody AddCourseTarget addCourseTarget) {
        System.out.println("LOG: addCourseTarget: " + addCourseTarget);
        return courseService.addCourseTarget(addCourseTarget);
    }

    @DeleteMapping("/courses/{id}/req/{name}")
    public ResponseEntity removeCourseReq(@PathVariable Long id, @PathVariable String name) {
        System.out.println("LOG: removeCourseReq(): req name: " + name);
        courseService.removeCourseReq(id, name);
        return ResponseEntity.ok("Course req deleted");
    }

    @DeleteMapping("/courses/{id}/goal/{name}")
    public ResponseEntity removeCourseGoal(@PathVariable Long id, @PathVariable String name) {
        System.out.println("LOG: removeCourseGoal(): goal name: " + name);
        courseService.removeCourseGoal(id, name);
        return ResponseEntity.ok("Course goal deleted");
    }

    @GetMapping("/courses/link/{permaLink}")
    public ResponseEntity getCourseByPermaLink(@PathVariable String permaLink) {
        return courseService.getCourseByPermalink(permaLink);
    }

    @GetMapping("/courses/category/{categoryName}")
    public ResponseEntity getCoursesByCategoryName(@PathVariable String categoryName) throws Exception {
        return courseService.getAllCoursesByRootCategory(categoryName);
    }

    @GetMapping("/courses/category/{categoryName}/{subCategoryName}")
    public ResponseEntity getCoursesBySubCategoryName(@PathVariable String categoryName, @PathVariable String subCategoryName) throws Exception {
        return courseService.getCoursesByCategory(categoryName, subCategoryName);
    }

    @GetMapping("/courses/topic/{topicName}")
    public ResponseEntity getCoursesByTopicName(@PathVariable String topicName) {
        return courseService.getCoursesByTopic(topicName);
    }

    @GetMapping("/courses/search")
    public List<ESCourse> searchCourses(@RequestParam("q") String search) {
        return courseService.searchCourses(search);
    }

    @GetMapping("/instructor/{id}/courses")
    public ResponseEntity getCoursesByInstructor(@PathVariable Long id) {
        List<Course> courses = courseService.getCoursesByInstructor(id);
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/instructor/{id}/courses/{courseId}")
    public ResponseEntity getCourseByInstructor(@PathVariable("id") Long instructorId, @PathVariable("courseId") Long courseId) {
        Course course = courseService.getCourseByInstructor(instructorId, courseId);
        return ResponseEntity.ok(course);
    }

}
