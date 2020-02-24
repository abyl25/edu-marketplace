package com.seniorproject.educationplatform.dto.course;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AddCourseSectionReq {
    @NotBlank
    private String name;
}
