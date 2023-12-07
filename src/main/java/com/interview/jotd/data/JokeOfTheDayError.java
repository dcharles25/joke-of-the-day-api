package com.interview.jotd.data;

import java.time.ZonedDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.Singular;

@Getter
@Setter
@Builder
public class JokeOfTheDayError {

    @Builder.Default 
    private ZonedDateTime timestamp = ZonedDateTime.now();
    
    private int status;

    private String error;
    
    @Singular 
    private List<String> messages;
}
