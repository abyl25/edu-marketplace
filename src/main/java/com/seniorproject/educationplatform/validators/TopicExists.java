package com.seniorproject.educationplatform.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = TopicValidator.class)
public @interface TopicExists {
    String message() default "Such topic does not exist";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
