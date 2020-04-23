package com.seniorproject.educationplatform.controllers;

import com.itextpdf.text.DocumentException;
import com.seniorproject.educationplatform.components.CertificateGenerator;
import com.seniorproject.educationplatform.dto.course.CourseOrderReqDto;
import com.seniorproject.educationplatform.dto.course.MultiCourseOrderReqDto;
import com.seniorproject.educationplatform.exceptions.CustomException;
import com.seniorproject.educationplatform.models.Course;
import com.seniorproject.educationplatform.services.CourseOrderService;
import com.seniorproject.educationplatform.services.FileService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class CourseRegistrationController {
    private CourseOrderService courseOrderService;
    private FileService fileService;
    private CertificateGenerator certificateGenerator;

    public CourseRegistrationController(CourseOrderService courseOrderService, FileService fileService, CertificateGenerator certificateGenerator) {
        this.courseOrderService = courseOrderService;
        this.fileService = fileService;
        this.certificateGenerator = certificateGenerator;
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

    /* Certificate Generator route */
    @GetMapping("/user/{id:[0-9]+}/courses/{courseId:[0-9]+}/certificate")
    public ResponseEntity<Object> getCertificate(@PathVariable Long id, @PathVariable Long courseId) throws IOException {
        Map<String,String> fileMap;
        try {
            fileMap = certificateGenerator.generate(id, courseId, null);
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
            throw new CustomException("Certificate generation failed", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        String basePath = fileMap.get("basePath");
        String filename = fileMap.get("filename");
        Resource resource = fileService.loadFileAsResource(filename, basePath, null);

        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Disposition", "attachment; filename=" + filename);
        headers.setContentDispositionFormData("attachment", filename);
        headers.setContentLength(Files.size(Paths.get(basePath + filename)));
        headers.setContentType(MediaType.APPLICATION_PDF);
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }

}
