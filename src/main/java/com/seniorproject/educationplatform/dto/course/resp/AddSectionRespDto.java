package com.seniorproject.educationplatform.dto.course.resp;

import com.seniorproject.educationplatform.models.CourseLecture;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AddSectionRespDto {
    private Long id;
    private String name;
    private Long courseId;
//    private List<CourseLecture> lectures = new ArrayList<>();
}
