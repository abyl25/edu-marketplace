package com.seniorproject.educationplatform.validators;

import com.seniorproject.educationplatform.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CategoryValidator implements ConstraintValidator<CategoryExists, String> {
    @Autowired
    private CategoryService categoryService;

    @Override
    public boolean isValid(String categoryName, ConstraintValidatorContext constraintValidatorContext) {
        return categoryService.categoryExists(categoryName);
    }
}
