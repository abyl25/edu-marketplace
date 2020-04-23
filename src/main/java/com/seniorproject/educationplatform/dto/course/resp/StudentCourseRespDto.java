package com.seniorproject.educationplatform.dto.course.resp;

import com.seniorproject.educationplatform.dto.review.ReviewRespDto;
import com.seniorproject.educationplatform.dto.user.resp.InstructorRespDto;
import com.seniorproject.educationplatform.models.*;
import lombok.Data;

import java.sql.Date;
import java.util.List;

@Data
public class StudentCourseRespDto {
    private Long id;
    private String title;
    private String subtitle;
    private String description;
    private Date addedDate;
    private Date lastUpdate;
    private String permaLink;
    private String imageName;
    private String imageFormat;
    private InstructorRespDto instructorDto;
    private List<CourseGoal> courseGoals;
    private List<CourseRequirement> courseRequirements;
    private List<CourseSection> sections;
    private List completedLectures;
    private ReviewRespDto reviewDto;
}
