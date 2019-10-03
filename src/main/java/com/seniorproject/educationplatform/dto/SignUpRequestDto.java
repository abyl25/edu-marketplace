package com.seniorproject.educationplatform.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class SignUpRequestDto {
    @NotBlank(message = "First name is empty")
    @Size(min = 2, message = "Minimum first name length: 2 characters")
//    @Pattern(regexp = "^[a-z.A-Z]+$", message = "No numbers are allowed for first name")
    private String firstName;

    @NotBlank(message = "Last name is empty")
    @Size(min = 2, message = "Minimum last name length: 2 characters")
//    @Pattern(regexp = "^[a-z.A-Z]+$", message = "No numbers are allowed for last name")
    private String lastName;

    @NotBlank(message = "Username is empty")
    @Size(min = 2, message = "Minimum username length: 2 characters")
//    @Pattern(regexp = "^[a-z.A-Z]+$", message = "No numbers are allowed for username")
    private String userName;

    @Email(message = "Invalid email")
    @NotBlank(message = "Email is empty")
    private String email;

    @NotBlank(message = "Password is empty")
    @Size(min = 5, message = "Minimum password length: 5 characters")
    private String password;

    private int userType; // admin = 1,  instructor = 2, student = 3
}
