package com.seniorproject.educationplatform.controllers;

import com.seniorproject.educationplatform.dto.course.resp.CoursesRespDto;
import com.seniorproject.educationplatform.models.ESModels.ESCourse;
import com.seniorproject.educationplatform.dto.course.*;
import com.seniorproject.educationplatform.dto.course.resp.AddLectureRespDto;
import com.seniorproject.educationplatform.dto.course.resp.AddSectionRespDto;
import com.seniorproject.educationplatform.models.Course;
import com.seniorproject.educationplatform.services.CourseOrderService;
import com.seniorproject.educationplatform.services.CourseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

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
    public ResponseEntity<List<CoursesRespDto>> getCourses(@RequestParam(value="status", required=false) String status, @RequestParam(value="page", required=false) Integer page) {
//        if (page != null) {
//            Pageable pageable = PageRequest.of(page, 10);
//            return courseService.getCoursesByPage(pageable);
//        }
        List<CoursesRespDto> courses = courseService.getCourses(status);
        CoursesRespDto course = courses.get(0);
        System.out.println("category: " + course.getCategory());
        System.out.println("topic: " + course.getTopic());
        course.getInstructor();
        course.getCourseGoals();

        return ResponseEntity.ok(courses); // new ArrayList<>()
    }

    @GetMapping("/courses/evict")
    public String evictCoursesCache() {
        courseService.evictCoursesCache();
        return "evicted";
    }

    @GetMapping("/courses/{courseId:[0-9]+}")
    public ResponseEntity<Object> getCourseById(@PathVariable Long courseId) {
        Optional<Course> optionalCourse = courseService.getCourseById(courseId);
        if (optionalCourse.isPresent()) {
            Course course = optionalCourse.get();
            return ResponseEntity.ok(course);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/courses")
    public ResponseEntity<Object> createCourse(@Valid @RequestBody CreateCourseReq createCourseReq) {
        System.out.println("LOG: CreateCourseReq: " + createCourseReq);
        Course newCourse = courseService.createCourse(createCourseReq);
        return ResponseEntity.ok(newCourse);
    }

    @DeleteMapping("/courses/{courseId:[0-9]+}")
    public ResponseEntity<Object> removeCourse(@PathVariable Long courseId) {
        courseService.removeCourse(courseId);
        return ResponseEntity.ok("Course removed");
    }

    @PutMapping("/courses/{courseId:[0-9]+}")
    public ResponseEntity<Object> updateCourseMainInfo(@PathVariable Long courseId, @Valid @RequestBody CreateCourseReq updateCourse) {
        System.out.println("LOG: updateCourseReq: " + updateCourse);
        return courseService.updateCourseMainInfo(courseId, updateCourse);
    }

    @GetMapping("/courses/{id:[0-9]+}/target")
    public ResponseEntity<Map<String, Object>> getCourseTarget(@PathVariable Long id) {
        return courseService.getCourseTarget(id);
    }

    @RequestMapping(value = "/courses/reqs", method = RequestMethod.POST, consumes="application/json", produces = "application/json")
    public ResponseEntity<Object> addCourseReqs(@Valid @RequestBody AddCourseRequirement courseReq) {
        courseService.addCourseReqs(courseReq);
        return ResponseEntity.ok("Course requirements added");
    }

    @RequestMapping(value = "/courses/goals", method = RequestMethod.POST, consumes="application/json", produces = "application/json")
    public ResponseEntity<Object> addCourseGoals(@Valid @RequestBody AddCourseGoal courseGoal) {
        courseService.addCourseGoals(courseGoal);
        return ResponseEntity.ok("Course goals added");
    }

    @RequestMapping(value = "/courses/target", method = RequestMethod.POST, consumes="application/json", produces = "application/json")
    public ResponseEntity<Object> addCourseTarget(@Valid @RequestBody AddCourseTarget addCourseTarget) {
        System.out.println("LOG: addCourseTarget: " + addCourseTarget);
        return courseService.addCourseTarget(addCourseTarget);
    }

    @DeleteMapping("/courses/{id:[0-9]+}/req/{name}")
    public ResponseEntity<Object> removeCourseReq(@PathVariable Long id, @PathVariable String name) {
        System.out.println("LOG: removeCourseReq(): req name: " + name);
        courseService.removeCourseReq(id, name);
        return ResponseEntity.ok("Course req deleted");
    }

    @DeleteMapping("/courses/{id:[0-9]+}/goal/{name}")
    public ResponseEntity<Object> removeCourseGoal(@PathVariable Long id, @PathVariable String name) {
        System.out.println("LOG: removeCourseGoal(): goal name: " + name);
        courseService.removeCourseGoal(id, name);
        return ResponseEntity.ok("Course goal deleted");
    }

    /* Course Section */
    @PostMapping("/courses/{id:[0-9]+}/section") // {sectionName:[A-Za-z0-9]+}
    public ResponseEntity<Object> addCourseSection(@PathVariable Long id, @RequestBody AddCourseSectionReq addCourseSectionReq) { // @PathVariable String sectionName
        AddSectionRespDto section = courseService.addCourseSection(id, addCourseSectionReq.getName());
        Map<String, Object> response = new HashMap<>();
        response.put("section", section);
        response.put("message", "Section added");
        return ResponseEntity.ok(response);
    }

    @PutMapping("/courses/{id:[0-9]+}/section/{sId:[0-9]+}")
    public ResponseEntity<Object> updateCourseSection(@PathVariable Long id, @PathVariable("sId") Long sectionId, @RequestBody AddCourseSectionReq addCourseSectionReq) {
        AddSectionRespDto section = courseService.updateCourseSection(id, sectionId, addCourseSectionReq.getName());
        Map<String, Object> response = new HashMap<>();
        response.put("section", section);
        response.put("message", "Section updated");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/courses/{id:[0-9]+}/section/{sId:[0-9]+}")
    public ResponseEntity<Object> deleteCourseSection(@PathVariable Long id, @PathVariable("sId") Long sectionId) {
        courseService.deleteCourseSection(id, sectionId);
        return ResponseEntity.ok("Course section deleted");
    }

    /* Course Lecture */
    @PostMapping("/courses/{id:[0-9]+}/section/{sId:[0-9]+}/lecture") // /{lName:[A-Za-z0-9]}
    public ResponseEntity<Object> addCourseLecture(@PathVariable Long id, @PathVariable("sId") Long sectionId, @RequestBody AddCourseSectionReq addCourseSectionReq ) { // @PathVariable("lName") String lectureName
        AddLectureRespDto lecture = courseService.addCourseLecture(id, sectionId, addCourseSectionReq.getName()); // lectureName
        Map<String, Object> response = new HashMap<>();
        response.put("lecture", lecture);
        response.put("message", "Lecture added");
        return ResponseEntity.ok(response);
    }

    @PutMapping("/courses/{id:[0-9]+}/section/{sId:[0-9]+}/lecture/{lId:[0-9]+}")
    public ResponseEntity<Object> updateCourseLecture(@PathVariable Long id, @PathVariable("sId") Long sectionId, @PathVariable("lId") Long lectureId, @RequestBody AddCourseSectionReq addCourseSectionReq) {
        AddLectureRespDto lecture = courseService.updateCourseLecture(id, sectionId, lectureId, addCourseSectionReq.getName());
        Map<String, Object> response = new HashMap<>();
        response.put("lecture", lecture);
        response.put("message", "Lecture updated");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/courses/{id:[0-9]+}/section/{sId:[0-9]+}/lecture/{lId:[0-9]+}")
    public ResponseEntity<Object> deleteCourseLecture(@PathVariable Long id, @PathVariable("sId") Long sectionId, @PathVariable("lId") Long lectureId) {
        courseService.deleteCourseLecture(id, sectionId, lectureId);
        return ResponseEntity.ok("Course lecture deleted");
    }

    
    @PatchMapping("/courses/{courseId:[0-9]+}/status/{status}")
    public ResponseEntity<Object> updateCourseByStatus(@PathVariable Long courseId, @PathVariable String status) {
        return courseService.updateCourseStatus(courseId, status);
    }

    @GetMapping("/courses/link/{permaLink}")
    public ResponseEntity<Object> getCourseByPermaLink(@PathVariable String permaLink) {
        return courseService.getCourseByPermalink(permaLink);
    }

    @GetMapping("/courses/category/{categoryName}")
    public ResponseEntity<Object> getCoursesByCategoryName(@PathVariable String categoryName) throws Exception {
        return courseService.getAllCoursesByRootCategory(categoryName);
    }

    @GetMapping("/courses/category/{categoryName}/{subCategoryName}")
    public ResponseEntity<Object> getCoursesBySubCategoryName(@PathVariable String categoryName, @PathVariable String subCategoryName) throws Exception {
        return courseService.getCoursesByCategory(categoryName, subCategoryName);
    }

//    @GetMapping("/courses/category/{categoryName}/{subCategoryName}/count")
//    public ResponseEntity getCoursesBySubCategoryNameCount(@PathVariable String categoryName, @PathVariable String subCategoryName) {
//        return courseService.getCoursesCountByCategory();
//    }

    @GetMapping("/courses/topic/{topicName}")
    public ResponseEntity<Object> getCoursesByTopicName(@PathVariable String topicName) {
        return courseService.getCoursesByTopic(topicName);
    }

    @GetMapping("/courses/search")
    public List<ESCourse> searchCourses(@RequestParam("q") String search) {
        return courseService.searchCourses(search);
    }

    @GetMapping("/instructor/{id}/courses")
    public ResponseEntity<Object> getCoursesByInstructor(@PathVariable Long id, @RequestParam(value="page", required=false) Integer page) {
        List<Course> courses = courseService.getCoursesByInstructor(id, page);
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/instructor/{id}/courses/count")
    public ResponseEntity<Object> getCoursesCountByInstructor(@PathVariable Long id) {
        int courseCount = courseService.getCoursesCountByInstructor(id);
        return ResponseEntity.ok(courseCount);
    }

    @GetMapping("/instructor/{id:[0-9]+}/courses/{courseId:[0-9]+}")
    public ResponseEntity<Object> getCourseByInstructor(@PathVariable("id") Long instructorId, @PathVariable("courseId") Long courseId) {
        System.out.println("LOG: CourseController: getCourseByInstructor(), courseId: " + courseId);
        Course course = courseService.getCourseByInstructor(instructorId, courseId);
        return ResponseEntity.ok(course);
    }

    @GetMapping("/instructor/{id:[0-9]+}/courses/{courseId:[0-9]+}/checkAccess")
    public ResponseEntity<Object> checkInstructorAccess(@PathVariable("id") Long instructorId, @PathVariable("courseId") Long courseId) {
        System.out.println("LOG: CourseController: checkInstructorAccess(), courseId: " + courseId);
        boolean isInstructorsCourse = courseService.isInstructorsCourse(instructorId, courseId);
        System.out.println("LOG: CourseController: isInstructorsCourse: " + isInstructorsCourse);
        return ResponseEntity.ok(isInstructorsCourse);
    }

}
