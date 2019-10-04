package com.seniorproject.educationplatform.dto;

import com.seniorproject.educationplatform.annotations.MySequence;
import lombok.Data;

import javax.validation.GroupSequence;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@GroupSequence({LoginRequestDto.class, MySequence.class})
public class LoginRequestDto {
    @NotBlank(message = "Username is empty")
    @Size(min = 2, message = "Minimum password length: 2 characters", groups = MySequence.First.class)
    @Pattern(regexp = "^[a-z.A-Z]+$", message = "No numbers are allowed for username", groups = MySequence.Second.class)
    private String userName;

    @NotBlank(message = "Password is empty")
    @Size(min = 5, message = "Minimum password length: 5 characters", groups = MySequence.First.class)
    private String password;

    private int userType; // student = 1, instructor = 2

}
