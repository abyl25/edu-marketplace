package com.seniorproject.educationplatform.controllers;

import com.seniorproject.educationplatform.dto.review.AddReviewReqDto;
import com.seniorproject.educationplatform.models.Review;
import com.seniorproject.educationplatform.services.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ReviewController {
    private ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/course/{id}/reviews")
    public ResponseEntity<Object> getCourseReviews(@PathVariable("id") Long courseId) {
        List<Review> reviews = reviewService.getCourseReviews(courseId);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/reviews/{id}")
    public ResponseEntity<Object> getCourseReview(@PathVariable Long id) {
        Review review = reviewService.getReviewById(id);
        return ResponseEntity.ok(review);
    }

    @PostMapping("/reviews")
    public ResponseEntity<Object> addCourseReview(@RequestBody AddReviewReqDto addReviewReqDto) {
        Review review = reviewService.addCourseReview(addReviewReqDto);
        return ResponseEntity.ok(review);
    }

    @DeleteMapping("/reviews/{id}")
    public ResponseEntity<Object> deleteCourseReview(@PathVariable Long id) {
        reviewService.deleteCourseReview(id);
        return ResponseEntity.ok("Review deleted");
    }

    @PostMapping("/reviews/{id}/increment")
    public ResponseEntity<Object> incrementReviewHelpfulCount(@PathVariable Long id) {
        reviewService.incrementHelpfulCount(id);
        return ResponseEntity.ok("Review helpful count incremented");
    }

    @PostMapping("/reviews/{id}/decrement")
    public ResponseEntity<Object> decrementReviewHelpfulCount(@PathVariable Long id) {
        reviewService.decrementHelpfulCount(id);
        return ResponseEntity.ok("Review helpful count decremented");
    }

    @PostMapping("/reviews/{id}/markFeatured")
    public ResponseEntity<Object> toggleMarkAsFeatured(@PathVariable Long id) {
        reviewService.toggleMarkAsFeatured(id);
        return ResponseEntity.ok("Review marked as featured");
    }

}
