package com.seniorproject.educationplatform.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = CategoryValidator.class)
public @interface CategoryExists {
    String message() default "Such category does not exist";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
