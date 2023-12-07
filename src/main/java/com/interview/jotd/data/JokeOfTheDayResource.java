package com.interview.jotd.data;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class JokeOfTheDayResource {
    private UUID id;

    private LocalDate date;
    
    private String joke;
    
    private Optional<String> description;
}
