package com.seniorproject.educationplatform.controllers;

import com.seniorproject.educationplatform.ESModels.ESCourse;
import com.seniorproject.educationplatform.dto.course.*;
import com.seniorproject.educationplatform.models.Course;
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
    private final CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/courses")
    public List<Course> getCourses() {
//        Pageable firstPage = PageRequest.of(0, 20); // Page<Course> // courseRepo.findAll(firstPage);
        return courseService.getCourses();
    }

    @GetMapping("/courses/{courseId}")
    public Course getCourseById(@PathVariable Long courseId) {
        Optional<Course> optionalCourse = courseService.getCourseById(courseId);
        Course course = null;
        if (optionalCourse.isPresent()) {
            course = optionalCourse.get();
        }
        return course;
    }

    @PostMapping("/courses")
    public ResponseEntity createCourse(@Valid @RequestBody CreateCourseReq createCourseReq) {
        System.out.println(createCourseReq);
//        Course newCourse = courseService.createCourse(createCourseReq);
        return ResponseEntity.ok("newCourse");
    }

    @PutMapping("/courses")
    public ResponseEntity addCourseInfo(@Valid @RequestBody AddCourseInfoDto addCourseInfoDto) {
        Course updatedCourse = courseService.addCourseInfo(addCourseInfoDto);
        return ResponseEntity.ok(updatedCourse);
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
        return courseService.addCourseTarget(addCourseTarget);
    }

    @DeleteMapping("/courses/{courseId}")
    public void removeCourse(@PathVariable Long courseId) {
        courseService.removeCourse(courseId);
    }

    @GetMapping("/courses/link/{permaLink}")
    public Course getCourseByPermaLink(@PathVariable String permaLink) {
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
