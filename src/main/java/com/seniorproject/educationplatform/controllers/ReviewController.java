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
    public ResponseEntity getCourseReviews(@PathVariable("id") Long courseId) {
        List<Review> reviews = reviewService.getCourseReviews(courseId);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/reviews/{id}")
    public ResponseEntity getCourseReview(@PathVariable Long id) {
        Review review = reviewService.getReviewById(id);
        return ResponseEntity.ok(review);
    }

    @PostMapping("/reviews")
    public ResponseEntity addCourseReview(AddReviewReqDto addReviewReqDto) {
        return reviewService.addCourseReview(addReviewReqDto);
    }

    @DeleteMapping("/reviews/{id}")
    public ResponseEntity deleteCourseReview(@PathVariable Long id) {
        reviewService.deleteCourseReview(id);
        return ResponseEntity.ok("Review deleted");
    }

    @PostMapping("/reviews/{id}/increment")
    public ResponseEntity incrementReviewHelpfulCount(@PathVariable Long id) {
        reviewService.incrementHelpfulCount(id);
        return ResponseEntity.ok("Review helpful count incremented");
    }

    @PostMapping("/reviews/{id}/decrement")
    public ResponseEntity decrementReviewHelpfulCount(@PathVariable Long id) {
        reviewService.decrementHelpfulCount(id);
        return ResponseEntity.ok("Review helpful count decremented");
    }

    @PostMapping("/reviews/{id}/markFeatured")
    public ResponseEntity toggleMarkAsFeatured(@PathVariable Long id) {
        reviewService.toggleMarkAsFeatured(id);
        return ResponseEntity.ok("Review marked as featured");
    }

}
