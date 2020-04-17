package com.seniorproject.educationplatform.controllers;

import com.seniorproject.educationplatform.dto.lecture.AddCommentReqDto;
import com.seniorproject.educationplatform.models.Comment;
import com.seniorproject.educationplatform.services.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CommentController {
    private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/lecture/{lectureId}/comments")
    public ResponseEntity<Object> getCommentsByLecture(@PathVariable Long lectureId) {
        List<Comment> comments = commentService.getCommentsByLecture(lectureId);
        return ResponseEntity.ok(comments);
    }

//    @GetMapping("/lecture/{lectureId}/root-comments")
//    public ResponseEntity<Object> getRootCommentsByLecture(@PathVariable Long lectureId) {
//        List<Comment> comments = postService.getRootCommentsByLecture(lectureId);
//        return ResponseEntity.ok(comments);
//    }

    @PostMapping("/lecture/{lectureId}/comments")
    public ResponseEntity<Object> addComment(@PathVariable Long lectureId, @RequestBody AddCommentReqDto addCommentReqDto) {
        Comment comment = commentService.addComment(lectureId, addCommentReqDto);
        return ResponseEntity.ok(comment);
    }

}
