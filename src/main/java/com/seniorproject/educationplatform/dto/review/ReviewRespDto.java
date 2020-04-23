package com.seniorproject.educationplatform.dto.review;

import lombok.Data;

@Data
public class ReviewRespDto {
    private Long id;
    private String content;
    private float rating;
}
