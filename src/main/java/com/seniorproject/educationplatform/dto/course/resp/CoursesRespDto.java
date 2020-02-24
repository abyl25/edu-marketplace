package com.seniorproject.educationplatform.dto.course.resp;

import com.seniorproject.educationplatform.dto.user.resp.InstructorRespDto;
import com.seniorproject.educationplatform.models.*;
import lombok.Data;

import java.sql.Date;
import java.util.List;

@Data
public class CoursesRespDto {
    private Long id;

    private String title;

    private String subtitle;

    private String description;

    private Level level;

    private String language;

    private String caption;

    private long price;

    private Date addedDate;

    private Date lastUpdate;

    private String permaLink;

    private CourseStatus status;

    private String image_name;

    private InstructorRespDto instructor;

    private Category category;

    private Topic topic;

    private List<CourseGoal> courseGoals;

    private List<CourseRequirement> courseRequirements;

    private List<Review> reviews;

}
