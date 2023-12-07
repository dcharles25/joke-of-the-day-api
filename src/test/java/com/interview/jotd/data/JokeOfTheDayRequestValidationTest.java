package com.interview.jotd.data;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

public class JokeOfTheDayRequestValidationTest {

    private final String DATE_FIELD = "2023-12-04";
    private final String JOKE_FIELD = "Writing unit tests is no laughing matter";
    private final String DESCRIPTION_FIELD = "A test joke";
    
    private Validator validator; 

    @BeforeEach
    public void init() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    public void whenJokeFieldProvided_shouldReturnNoViolations() {
        JokeOfTheDayRequest jokeRequest = new JokeOfTheDayRequest(DATE_FIELD, JOKE_FIELD, DESCRIPTION_FIELD);

        Set<ConstraintViolation<JokeOfTheDayRequest>> constraints = validator.validate(jokeRequest);

        assertTrue(constraints.isEmpty(), "Expect no constraint violations when joke field is not blank");
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", " "})
    public void whenJokeFieldBlank_shouldReturnConstraintViolations(String jokeCandidate) {
        JokeOfTheDayRequest jokeRequest = new JokeOfTheDayRequest(DATE_FIELD, jokeCandidate, DESCRIPTION_FIELD);

        Set<ConstraintViolation<JokeOfTheDayRequest>> constraints = validator.validate(jokeRequest);

        assertFalse(constraints.isEmpty(), "Expect a contraint violation when joke field is blank");
    }

    @Test
    public void whenDateFieldProvided_shouldReturnNoViolations() {
        JokeOfTheDayRequest jokeRequest = new JokeOfTheDayRequest(DATE_FIELD, JOKE_FIELD, DESCRIPTION_FIELD);

        Set<ConstraintViolation<JokeOfTheDayRequest>> constraints = validator.validate(jokeRequest);

        assertTrue(constraints.isEmpty(), "Expect no constraint violations when date field is not blank");
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", " "})
    public void whenDateFieldBlank_shouldReturnConstraintViolations(String dateCandidate) {
        JokeOfTheDayRequest jokeRequest = new JokeOfTheDayRequest(dateCandidate, JOKE_FIELD, DESCRIPTION_FIELD);

        Set<ConstraintViolation<JokeOfTheDayRequest>> constraints = validator.validate(jokeRequest);

        assertFalse(constraints.isEmpty(), "Expect a contraint violation when date field is blank");
    }

    @ParameterizedTest
    @ValueSource(strings = {"03-13-2023",  "2019-24-08", "3 Jun 2008 11:05:30", "20111203", "2023-12-04T10:15:30", "42"})
    public void whenDateNotInExpectedFormat_shouldReturnConstraintViolations(String dateCandidate) {
        JokeOfTheDayRequest jokeRequest = new JokeOfTheDayRequest(dateCandidate, JOKE_FIELD, DESCRIPTION_FIELD);

        Set<ConstraintViolation<JokeOfTheDayRequest>> constraints = validator.validate(jokeRequest);

        assertFalse(constraints.isEmpty(), "Expect a contraint violation when date field is not in ISO standard format");
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", " "})
    public void whenDescriptionFieldNotProvided_shouldReturnNoViolations(String descriptionCandidate) {
        JokeOfTheDayRequest jokeRequest = new JokeOfTheDayRequest(DATE_FIELD, JOKE_FIELD, descriptionCandidate);

        Set<ConstraintViolation<JokeOfTheDayRequest>> constraints = validator.validate(jokeRequest);

        assertTrue(constraints.isEmpty(), "Expect no constraint violations when description field is blank");
    }
}
