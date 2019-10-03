package com.seniorproject.educationplatform.dto;

import lombok.Data;

import javax.validation.GroupSequence;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.validation.groups.Default;

@Data
@GroupSequence({LoginRequestDto.class, LoginRequestDto.First.class, LoginRequestDto.Second.class})
public class LoginRequestDto {
    @NotBlank(message = "Username is empty")
    @Size(min = 2, message = "Minimum password length: 2 characters", groups = First.class)
//    @Pattern(regexp = "^[a-z.A-Z]+$", message = "No numbers are allowed for username")
    private String userName;

    @NotBlank(message = "Password is empty")
    @Size(min = 5, message = "Minimum password length: 5 characters", groups = First.class)
    private String password;

    private int userType; // student = 1, instructor = 2

    public interface First {}
    public interface Second {}
}
