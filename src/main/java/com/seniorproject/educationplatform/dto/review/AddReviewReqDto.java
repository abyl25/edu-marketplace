package com.seniorproject.educationplatform.dto.review;

import lombok.Data;

@Data
public class AddReviewReqDto {
    private Long studentId;
    private Long courseId;
    private String content;
    private float rating;
}
