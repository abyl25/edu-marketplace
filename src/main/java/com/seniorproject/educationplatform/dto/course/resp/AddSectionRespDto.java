package com.seniorproject.educationplatform.dto.course.resp;

import lombok.Data;

@Data
public class AddSectionRespDto {
    private Long id;
    private String name;
    private Long courseId;
}
