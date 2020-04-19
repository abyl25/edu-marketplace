package com.seniorproject.educationplatform.services;

import com.seniorproject.educationplatform.dto.review.AddReviewReqDto;
import com.seniorproject.educationplatform.exceptions.CustomException;
import com.seniorproject.educationplatform.models.Review;
import com.seniorproject.educationplatform.repositories.ReviewRepo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ReviewService {
    private ReviewRepo reviewRepo;
    private UserService userService;
    private CourseService courseService;
    private CourseOrderService courseOrderService;

    public ReviewService(ReviewRepo reviewRepo, UserService userService, CourseService courseService, CourseOrderService courseOrderService) {
        this.reviewRepo = reviewRepo;
        this.userService = userService;
        this.courseService = courseService;
        this.courseOrderService = courseOrderService;
    }

    public List<Review> getCourseReviews(Long courseId) {
        return reviewRepo.findByCourseId(courseId);
    }

    public Review getReviewById(Long reviewId) {
        return reviewRepo.findById(reviewId).orElse(null);
    }

    public Review addCourseReview(AddReviewReqDto addReviewReqDto) {
        boolean purchased = courseOrderService.checkIfStudentPurchasedCourse(addReviewReqDto.getStudentId(), addReviewReqDto.getCourseId());
        if (!purchased) {
            throw new CustomException("Student can't write review", HttpStatus.UNPROCESSABLE_ENTITY);
        }
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

    public int getReviewCountByCourse(Long reviewId) {
        return reviewRepo.countByCourseId(reviewId);
    }

    public float getAverageCourseRating(Long courseId) {
        List<Review> reviews = getCourseReviews(courseId);
        Float sum = reviews.stream().map(Review::getRating).reduce((float) 0, Float::sum);
        return sum/reviews.size();
    }

    // Mappers
    private Review reviewDtoToEntity(AddReviewReqDto addReviewReqDto) {
        Review review = new Review();
        review.setStudent(userService.getUserById(addReviewReqDto.getStudentId()));
        review.setCourse(courseService.getCourseById(addReviewReqDto.getCourseId()).get());
        review.setContent(addReviewReqDto.getContent());
        review.setRating(addReviewReqDto.getRating());
        review.setAddedDate(new Date());
        return review;
    }

}
