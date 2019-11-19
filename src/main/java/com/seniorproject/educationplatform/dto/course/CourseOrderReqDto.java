package com.seniorproject.educationplatform.dto.course;

import lombok.Data;

@Data
public class CourseOrderReqDto {
    private Long studentId;
    private Long courseId;
    private long price;
//    private String paymentType;
}
