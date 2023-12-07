package com.interview.jotd.data.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class JokeDataAccessException extends RuntimeException {

    public JokeDataAccessException(String message, String recordId) {
        super(new StringBuilder()
                    .append(message)
                    .append(" - ")
                    .append(recordId).toString());
    }

    public JokeDataAccessException(String message, Exception exception) {
        super(message, exception);
    }

    public JokeDataAccessException(String message, String recordId, Exception exception) {
        super(new StringBuilder()
                    .append(message)
                    .append(" - ")
                    .append(recordId).toString(), exception);
    }
}
