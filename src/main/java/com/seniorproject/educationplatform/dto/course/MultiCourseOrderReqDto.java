package com.seniorproject.educationplatform.dto.course;

import lombok.Data;

import java.util.List;

@Data
public class MultiCourseOrderReqDto {
    private Long userId;
    private List<Long> courseIds;
    private String name;
    private String cardNumber;
    private String expiration;
    private String security;
}
