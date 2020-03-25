package com.seniorproject.educationplatform.dto.course;

import com.seniorproject.educationplatform.validators.CategoryExists;
import com.seniorproject.educationplatform.validators.MySequence;
import com.seniorproject.educationplatform.validators.TopicExists;
import lombok.Data;

import javax.validation.GroupSequence;
import javax.validation.constraints.*;

@Data
@GroupSequence({CreateCourseReq.class, MySequence.class})
public class CreateCourseReq {

    @NotNull(message = "Instructor is empty")
    private Long instructorId;

    @NotBlank(message = "Title is empty")
    @Size(min = 2, message = "Minimum title length: 2 characters", groups = MySequence.First.class)
//    @TitleExists(groups = MySequence.Second.class)
    private String title;

    @NotBlank(message = "Subtitle is empty")
    @Size(min = 2, message = "Minimum subtitle length: 2 characters", groups = MySequence.First.class)
    private String subtitle;

    @NotBlank(message = "Description is empty")
    @Size(min = 5, message = "Minimum description length: 5 characters", groups = MySequence.First.class)
    private String description;

    @NotBlank(message = "Choose level")
    private String level;

    @NotBlank(message = "Choose language")
    private String language;

    @NotNull(message = "Set course price")
    @PositiveOrZero(message = "Price can't be negative", groups = MySequence.First.class)
    @Max(value = 200, groups = MySequence.Second.class)
    private Long price;

    @NotBlank(message = "Choose category")
    @CategoryExists(groups = MySequence.First.class)
    private String category;

//    @NotBlank(message = "Subcategory is empty")
//    @CategoryExists(groups = MySequence.First.class)
//    private String subcategory;

    @NotBlank(message = "Choose topic")
    @TopicExists(groups = MySequence.First.class)
    private String topic;

}
