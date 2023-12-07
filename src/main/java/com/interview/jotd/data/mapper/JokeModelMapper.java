package com.interview.jotd.data.mapper;

import com.interview.jotd.data.JokeOfTheDay;
import com.interview.jotd.data.JokeOfTheDayResource;

public class JokeModelMapper {
    
    public static JokeOfTheDay toModel(JokeOfTheDayResource resourceObject) {
        return JokeOfTheDay.builder()
                    .date(resourceObject.getDate())
                    .joke(resourceObject.getJoke())
                    .description(resourceObject
                                    .getDescription()
                                    .orElse(null))
                    .build();
    }
}
