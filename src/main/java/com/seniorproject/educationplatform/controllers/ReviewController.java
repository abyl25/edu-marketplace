package com.seniorproject.educationplatform.controllers;

import com.seniorproject.educationplatform.dto.review.AddReviewReqDto;
import com.seniorproject.educationplatform.models.Review;
import com.seniorproject.educationplatform.services.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/review")
public class ReviewController {
    private ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping
    public ResponseEntity getReviews() {
        List<Review> reviews = reviewService.getReviews();
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/{id}")
    public ResponseEntity getReviewById(@PathVariable Long id) {
        Review review = reviewService.getReviewById(id);
        return ResponseEntity.ok(review);
    }

    @PostMapping
    public ResponseEntity addCourseReview(AddReviewReqDto addReviewReqDto) {
        reviewService.addCourseReview(addReviewReqDto);
        return ResponseEntity.ok("Review added");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteCourseReview(@PathVariable Long id) {
        reviewService.deleteCourseReview(id);
        return ResponseEntity.ok("Review deleted");
    }

    @PostMapping("/{id}")
    public ResponseEntity incrementReviewHelpfulCount(@PathVariable Long id) {
        reviewService.incrementHelpfulCount(id);
        return ResponseEntity.ok("Review helpful count incremented");
    }

    @PostMapping("/{id}")
    public ResponseEntity decrementReviewHelpfulCount(@PathVariable Long id) {
        reviewService.decrementHelpfulCount(id);
        return ResponseEntity.ok("Review helpful count decremented");
    }

    @PostMapping("/{id}")
    public ResponseEntity toggleMarkAsFeatured(@PathVariable Long id) {
        reviewService.toggleMarkAsFeatured(id);
        return ResponseEntity.ok("Review marked as featured");
    }

}
