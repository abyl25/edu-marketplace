package com.seniorproject.educationplatform.dto.course;

import lombok.Data;

import java.util.List;

@Data
public class AddCourseTarget {
    List<AddCourseReq> reqs;
    List<AddCourseGoal> goals;
}
