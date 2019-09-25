package com.seniorproject.educationplatform.dto;

import com.seniorproject.educationplatform.models.Level;
import com.seniorproject.educationplatform.models.User;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.sql.Date;

@Data
public class AddCourseDto {
    private String title;
    private String subtitle;
    private Long instructorId;
    private String description;
    private String level;
    private String language;
    private String caption;
    private Long price;

}
