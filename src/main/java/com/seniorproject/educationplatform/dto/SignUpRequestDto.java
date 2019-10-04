package com.seniorproject.educationplatform.dto;

import com.seniorproject.educationplatform.annotations.MySequence;
import lombok.Data;

import javax.validation.GroupSequence;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@GroupSequence({SignUpRequestDto.class, MySequence.class})
public class SignUpRequestDto {
    @NotBlank(message = "First name is empty")
    @Size(min = 2, message = "Minimum first name length: 2 characters", groups = MySequence.First.class)
    @Pattern(regexp = "^[a-zA-Z]+$", message = "No numbers are allowed for first name", groups = MySequence.Second.class)
    private String firstName;

    @NotBlank(message = "Last name is empty")
    @Size(min = 2, message = "Minimum last name length: 2 characters", groups = MySequence.First.class)
    @Pattern(regexp = "^[a-zA-Z]+$", message = "No numbers are allowed for last name", groups = MySequence.Second.class)
    private String lastName;

    @NotBlank(message = "Username is empty")
    @Size(min = 2, message = "Minimum username length: 2 characters", groups = MySequence.First.class)
    @Pattern(regexp = "^[.a-zA-Z0-9]+$", message = "Allowed: [.a-zA-Z0-9]", groups = MySequence.Second.class)
    private String userName;

    @NotBlank(message = "Email is empty")
    @Email(message = "Invalid email", groups = MySequence.First.class)
    private String email;

    @NotBlank(message = "Password is empty")
    @Size(min = 5, message = "Minimum password length: 5 characters", groups = MySequence.First.class)
    private String password;

    private int userType; // admin = 1,  instructor = 2, student = 3
}
