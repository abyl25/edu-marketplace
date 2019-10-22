package com.seniorproject.educationplatform.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class NotEmptyFieldsValidator implements ConstraintValidator<NotEmptyFields, List<String>> {

   public boolean isValid(List<String> items, ConstraintValidatorContext context) {
      return items.stream().noneMatch(item -> item.trim().isEmpty());
   }

}
