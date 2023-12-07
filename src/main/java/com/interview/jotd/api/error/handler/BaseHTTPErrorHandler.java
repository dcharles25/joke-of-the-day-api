package com.interview.jotd.api.error.handler;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.StatusType;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.interview.jotd.data.JokeOfTheDayError;

@Provider
public class BaseHTTPErrorHandler implements ExceptionMapper<WebApplicationException> {

    @Override
    public Response toResponse(WebApplicationException exception) {
        Response response = exception.getResponse();
        StatusType statusType = response.getStatusInfo();

        JokeOfTheDayError responseBody = JokeOfTheDayError.builder()
                                                .status(statusType.getStatusCode())
                                                .error(statusType.getReasonPhrase())
                                                .message(exception.getMessage())
                                                .build();

        return Response.status(statusType)
                    .type(MediaType.APPLICATION_JSON)
                    .entity(responseBody)
                    .build();
    }
    
}
