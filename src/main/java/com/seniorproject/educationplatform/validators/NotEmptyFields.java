package com.seniorproject.educationplatform.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = NotEmptyFieldsValidator.class)
public @interface NotEmptyFields {
    String message() default "List can't contain empty values";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
