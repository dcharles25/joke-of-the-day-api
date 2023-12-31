package com.interview.jotd.data.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;


@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FormattedDateValidator.class)
public @interface FormattedDate {
    String message() default "{com.interview.jotd.data.validation.FormattedDate.message}";
    Class[] groups() default {};
    Class[] payload() default {};
    String pattern();
}
