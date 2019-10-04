package com.seniorproject.educationplatform.validators;

import com.seniorproject.educationplatform.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TopicValidator implements ConstraintValidator<TopicExists, String> {
   @Autowired
   CategoryService categoryService;

   @Override
   public boolean isValid(String topicName, ConstraintValidatorContext context) {
      return categoryService.topicExists(topicName);
   }
}
