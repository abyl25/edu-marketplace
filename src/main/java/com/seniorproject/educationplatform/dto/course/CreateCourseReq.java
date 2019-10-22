package com.seniorproject.educationplatform.dto.course;

import com.seniorproject.educationplatform.validators.MySequence;
import com.seniorproject.educationplatform.validators.TitleExists;
import lombok.Data;

import javax.validation.GroupSequence;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@GroupSequence({CreateCourseReq.class, MySequence.class})
public class CreateCourseReq {

    @NotBlank(message = "Title is empty")
    @Size(min = 5, message = "Minimum title length: 5 characters", groups = MySequence.First.class)
    @TitleExists(groups = MySequence.Second.class)
    private String title;

    @NotNull(message = "Category is empty")
    private Long categoryId;

    @NotNull(message = "Instructor is empty")
    private Long instructorId;

}
