package com.seniorproject.educationplatform.dto.user.resp;

import lombok.Data;

import java.io.Serializable;

@Data
public class InstructorRespDto implements Serializable {
    private String firstName;

    private String lastName;

    private String email;

    private String imageName;

    private int coursesCount;

    private int studentsCount;

    private int reviewsCount;
}
