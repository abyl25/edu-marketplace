package com.seniorproject.educationplatform.controllers;

import com.seniorproject.educationplatform.services.LectureService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class LectureController {
    private LectureService lectureService;

    public LectureController(LectureService lectureService) {
        this.lectureService = lectureService;
    }

    @GetMapping("/user/{userId}/course/{courseId}/lectures")
    public ResponseEntity<Object>  getUserCompletedLecturesByCourse(@PathVariable Long userId, @PathVariable Long courseId) {
        List completedLectures = lectureService.getUserCompletedLecturesByCourse(userId, courseId);
        return ResponseEntity.ok(completedLectures);
    }

    @PostMapping("/user/{userId}/lectures/{lectureId}")
    public ResponseEntity<Object> toggleCompleteLectures(@PathVariable Long userId, @PathVariable Long lectureId) {
        lectureService.toggleCompleteLecture(lectureId, userId);
        return ResponseEntity.ok("Lecture toggled");
    }

}
