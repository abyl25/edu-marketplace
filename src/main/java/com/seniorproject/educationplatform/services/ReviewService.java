package com.seniorproject.educationplatform.services;

import com.seniorproject.educationplatform.dto.review.AddReviewReqDto;
import com.seniorproject.educationplatform.models.Review;
import com.seniorproject.educationplatform.repositories.ReviewRepo;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ReviewService {
    private ReviewRepo reviewRepo;
    private UserService userService;
    private CourseService courseService;

    public ReviewService(ReviewRepo reviewRepo, UserService userService, CourseService courseService) {
        this.reviewRepo = reviewRepo;
        this.userService = userService;
        this.courseService = courseService;
    }

    public List<Review> getReviews() {
        return reviewRepo.findAll();
    }

    public Review getReviewById(Long reviewId) {
        return reviewRepo.findById(reviewId).orElse(null);
    }

    public Review addCourseReview(AddReviewReqDto addReviewReqDto) {
        Review review = reviewDtoToEntity(addReviewReqDto);
        return reviewRepo.save(review);
    }

    public void deleteCourseReview(Long reviewId) {
        reviewRepo.deleteById(reviewId);
    }

    public void incrementHelpfulCount(Long reviewId) {
        Review review = reviewRepo.findById(reviewId).get();
        review.setHelpfulCount(review.getHelpfulCount() + 1);
        reviewRepo.save(review);
    }

    public void decrementHelpfulCount(Long reviewId) {
        Review review = reviewRepo.findById(reviewId).get();
        review.setHelpfulCount(review.getHelpfulCount() - 1);
        reviewRepo.save(review);
    }

    public void toggleMarkAsFeatured(Long reviewId) {
        Review review = reviewRepo.findById(reviewId).get();
        if (review.isFeatured()) {
            review.setFeatured(false);
        } else {
            review.setFeatured(true);
        }
        reviewRepo.save(review);
    }

    // Mappers
    private Review reviewDtoToEntity(AddReviewReqDto addReviewReqDto) {
        Review review = new Review();
        review.setStudent(userService.getUserById(addReviewReqDto.getStudentId()).get());
        review.setCourse(courseService.getCourseById(addReviewReqDto.getCourseId()).get());
        review.setContent(addReviewReqDto.getContent());
        review.setRating(addReviewReqDto.getRating());
        review.setAddedDate(new Date());
        return review;
    }

}
