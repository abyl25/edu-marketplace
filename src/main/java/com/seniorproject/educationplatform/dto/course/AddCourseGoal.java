package com.seniorproject.educationplatform.dto.course;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AddCourseGoal {
    @NotBlank(message = "Goal name is empty")
    private String name;
    private Long courseId;
}
