package com.seniorproject.educationplatform.validators;

import com.seniorproject.educationplatform.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TitleValidator implements ConstraintValidator<TitleExists, String> {
   @Autowired
   private CourseService courseService;

   public boolean isValid(String courseTitle, ConstraintValidatorContext context) {
      return !courseService.courseTitleExists(courseTitle);
   }
}
