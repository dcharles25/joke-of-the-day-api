package com.interview.jotd.api.error.handler;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.interview.jotd.data.JokeOfTheDayError;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Provider
public class ConstraintViolationResponseHandler implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        log.warn("Recieved a bad request due to a data constraint violation", exception);

        JokeOfTheDayError responseBody = JokeOfTheDayError.builder()
                                    .status(BAD_REQUEST.getStatusCode())
                                    .error(BAD_REQUEST.getReasonPhrase())
                                    .messages(buildErrorMessageList(exception.getConstraintViolations()))
                                    .build();

        return Response.status(BAD_REQUEST)
                    .type(MediaType.APPLICATION_JSON)
                    .entity(responseBody)
                    .build();
    }

    private List<String> buildErrorMessageList(Set<ConstraintViolation<?>> violations) {
        return violations.stream()
                    .map(violation -> violation.getMessage())
                    .collect(Collectors.toList());
    }
}
