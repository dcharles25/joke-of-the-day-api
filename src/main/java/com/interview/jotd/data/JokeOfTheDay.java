package com.interview.jotd.data;

import java.time.LocalDate;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter 
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JokeOfTheDay {

    private UUID id;

    private LocalDate date;
    
    private String joke;
    
    private String description;
}
