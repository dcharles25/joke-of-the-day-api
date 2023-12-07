package com.interview.jotd.data.mapper;

import java.time.LocalDate;
import java.util.Optional;

import com.interview.jotd.data.JokeOfTheDay;
import com.interview.jotd.data.JokeOfTheDayRequest;
import com.interview.jotd.data.JokeOfTheDayResource;

public class JokeResourceMapper {
    
    public static JokeOfTheDayResource toResource(JokeOfTheDayRequest requestObject) {
        return JokeOfTheDayResource.builder()
                        .date(LocalDate.parse(requestObject.getDate()))
                        .joke(requestObject.getJoke())
                        .description(Optional.ofNullable(
                                requestObject.getDescription()))
                        .build();
    }

    public static JokeOfTheDayResource toResource(JokeOfTheDay model) {
        return JokeOfTheDayResource.builder()
                        .id(model.getId())
                        .date(model.getDate())
                        .joke(model.getJoke())
                        .description(Optional.ofNullable(
                                model.getDescription()))
                        .build();
    }
}
