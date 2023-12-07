package com.interview.jotd.api.error.handler;

import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.interview.jotd.data.JokeOfTheDayError;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Provider
public class UnknownErrorResponseHandler implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception exception) {
        log.error("An unknown error has occurred while processing a request", exception);

        JokeOfTheDayError responseBody = JokeOfTheDayError.builder()
                                    .status(INTERNAL_SERVER_ERROR.getStatusCode())
                                    .error(INTERNAL_SERVER_ERROR.getReasonPhrase())
                                    .message("Unable to complete request. An unexpected error has occurred.")
                                    .build();

        return Response.status(INTERNAL_SERVER_ERROR)
                    .type(MediaType.APPLICATION_JSON)
                    .entity(responseBody)
                    .build();
    }
}
