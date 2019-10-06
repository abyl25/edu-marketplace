package com.seniorproject.educationplatform.dto;

import com.seniorproject.educationplatform.validators.MySequence;
import com.seniorproject.educationplatform.validators.PasswordMatches;
import lombok.Data;

import javax.validation.GroupSequence;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@PasswordMatches
@GroupSequence({PasswordUpdateDto.class, MySequence.class})
public class PasswordUpdateDto {
    @NotBlank(message = "Password is empty")
    @Size(min = 5, message = "Minimum password length: 5 characters", groups = MySequence.First.class)
    private String password;

    @NotBlank(message = "matchPassword is empty")
    @Size(min = 5, message = "Match password length: 5 characters", groups = MySequence.First.class)
    private String matchPassword;

}
