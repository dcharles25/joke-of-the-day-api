package com.interview.jotd.data.exception;

import java.time.LocalDate;

import javax.ws.rs.BadRequestException;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class JokeDateConflictException extends BadRequestException {

    public JokeDateConflictException(String message, LocalDate date) {
        super(new StringBuilder()
                        .append(message)
                        .append(" - ")
                        .append(date.toString())
                        .toString());
    }
}
