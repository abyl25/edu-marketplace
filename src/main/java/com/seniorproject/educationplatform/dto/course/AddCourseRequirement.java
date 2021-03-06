package com.seniorproject.educationplatform.dto.course;

import com.seniorproject.educationplatform.validators.NotEmptyFields;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class AddCourseRequirement {
    @NotBlank(message = "Course Id is empty")
    private Long courseId;

    @NotEmptyFields(message = "One of requirements is empty")
    private List<String> reqs;

    public AddCourseRequirement() {}

    public AddCourseRequirement(Long courseId, List<String> reqs) {
        this.courseId = courseId;
        this.reqs = reqs;
    }

}
