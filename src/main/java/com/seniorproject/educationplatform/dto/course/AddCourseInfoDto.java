package com.seniorproject.educationplatform.dto.course;

import com.seniorproject.educationplatform.validators.CategoryExists;
import com.seniorproject.educationplatform.validators.MySequence;
import com.seniorproject.educationplatform.validators.TitleExists;
import com.seniorproject.educationplatform.validators.TopicExists;
import lombok.Data;

import javax.validation.GroupSequence;
import javax.validation.constraints.*;

@Data
@GroupSequence({AddCourseInfoDto.class, MySequence.class})
public class AddCourseInfoDto {
    @NotNull(message = "Course Id is empty")
    private Long courseId;

    @NotBlank(message = "Title is empty")
    @Size(min = 5, message = "Minimum title length: 5 characters", groups = MySequence.First.class)
    @TitleExists(groups = MySequence.Second.class)
    private String title;

    @NotBlank(message = "Subtitle is empty")
    @Size(min = 5, message = "Minimum subtitle length: 5 characters", groups = MySequence.First.class)
    private String subtitle;

    @NotNull(message = "Instructor is empty")
    private Long instructorId;

    @NotBlank(message = "Description is empty")
    @Size(min = 10, message = "Minimum description length: 10 characters", groups = MySequence.First.class)
    private String description;

    @NotBlank(message = "Level is empty")
    private String level;

    @NotBlank(message = "Language is empty")
    private String language;

    @NotBlank(message = "Caption is empty")
    private String caption;

    @NotNull(message = "Price is empty")
    @PositiveOrZero(message = "Price can't be negative", groups = MySequence.First.class)
    @Max(value = 200, groups = MySequence.Second.class)
    private Long price;

    @NotBlank(message = "Category is empty")
    @CategoryExists(groups = MySequence.First.class)
    private String category;

//    @NotBlank(message = "Subcategory is empty")
//    @CategoryExists(groups = MySequence.First.class)
//    private String subcategory;

    @NotBlank(message = "Topic is empty")
    @TopicExists(groups = MySequence.First.class)
    private String topic;

}
