package com.interview.jotd.data.validation;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class FormattedDateValidator implements ConstraintValidator<FormattedDate, String> {

    private String pattern;

    @Override
    public void initialize(FormattedDate constraint) {
        this.pattern = constraint.pattern();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try {
            boolean isValid = false;
            
            if (value != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
                formatter.parse(value, LocalDate::from);
    
                isValid = true;
            } 
            
            return isValid;
        } catch (DateTimeParseException e) {
            log.debug("Failed to properly validate that a {} matches given date pattern {}", value, pattern, e);
            return false;
        }
    }
}
