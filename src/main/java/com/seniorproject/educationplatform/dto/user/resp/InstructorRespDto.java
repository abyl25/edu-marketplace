package com.seniorproject.educationplatform.dto.user.resp;

import lombok.Data;

@Data
public class InstructorRespDto {
    private String firstName;

    private String lastName;

    private String email;

    private String imageName;

    private int coursesCount;

    private int studentsCount;

    private int reviewsCount;
}
