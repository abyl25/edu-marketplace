package com.seniorproject.educationplatform.validators;

import com.seniorproject.educationplatform.dto.auth.PasswordUpdateDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

   public boolean isValid(Object obj, ConstraintValidatorContext context) {
      PasswordUpdateDto passwordUpdateDto = (PasswordUpdateDto)obj;
      return passwordUpdateDto.getPassword().equals(passwordUpdateDto.getMatchPassword());
   }

}
