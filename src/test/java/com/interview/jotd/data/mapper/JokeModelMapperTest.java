package com.interview.jotd.data.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.interview.jotd.data.JokeOfTheDay;
import com.interview.jotd.data.JokeOfTheDayResource;

public class JokeModelMapperTest {

    @Test
    public void toModel_whenGivenResourceObject_shouldReturnModelObject() {
        JokeOfTheDayResource jokeResource = JokeOfTheDayResource.builder()
                                                .date(LocalDate.parse("2015-10-23"))
                                                .joke("Unit tests are never a joke")
                                                .description(Optional.of("A test joke description"))
                                                .build();

        JokeOfTheDay jokeModel = JokeModelMapper.toModel(jokeResource);

        assertNotNull(jokeModel);
        assertEquals(jokeResource.getDate(), jokeModel.getDate());
        assertEquals(jokeResource.getJoke(), jokeModel.getJoke());
        assertEquals(jokeResource.getDescription().get(), jokeModel.getDescription());
    }

    @Test
    public void toModel_whenGivenNoDescription_shouldReturnModelWithNullDescription() {
        JokeOfTheDayResource jokeResource = JokeOfTheDayResource.builder()
                                                .date(LocalDate.parse("2015-10-23"))
                                                .joke("Unit tests are never a joke")
                                                .description(Optional.empty())
                                                .build();

        JokeOfTheDay jokeModel = JokeModelMapper.toModel(jokeResource);

        assertNotNull(jokeModel);
        assertNull(jokeModel.getDescription());
    }
}
