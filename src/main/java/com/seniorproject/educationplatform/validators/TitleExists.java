package com.seniorproject.educationplatform.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = TitleValidator.class)
public @interface TitleExists {
    String message() default "Title already exists! Choose another one";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
