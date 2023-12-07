package com.interview.jotd.data.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.interview.jotd.data.JokeOfTheDay;
import com.interview.jotd.data.JokeOfTheDayRequest;
import com.interview.jotd.data.JokeOfTheDayResource;

public class JokeResourceMapperTest {

    private final String DATE_FIELD = "2023-12-04";
    private final String JOKE_FIELD = "Writing unit tests is no laughing matter";
    private final String DESCRIPTION_FIELD = "A test joke";

    @Test
    public void toResource_whenGivenModelObject_shouldReturnResourceObject() {
        JokeOfTheDay jokeModel = JokeOfTheDay.builder()
                                        .id(UUID.randomUUID())
                                        .date(LocalDate.parse(DATE_FIELD))
                                        .joke(JOKE_FIELD)
                                        .description(DESCRIPTION_FIELD)
                                        .build();

        JokeOfTheDayResource jokeResource = JokeResourceMapper.toResource(jokeModel);

        assertNotNull(jokeResource);
        assertEquals(jokeModel.getDate(), jokeResource.getDate());
        assertEquals(jokeModel.getJoke(), jokeResource.getJoke());
        assertEquals(jokeModel.getDescription(), jokeResource.getDescription().get());
    }

    @Test
    public void toResource_whenGivenRequestObject_shouldReturnResourceObject() {
        JokeOfTheDayRequest jokeRequest = new JokeOfTheDayRequest(DATE_FIELD, JOKE_FIELD, DESCRIPTION_FIELD);

        JokeOfTheDayResource jokeResource = JokeResourceMapper.toResource(jokeRequest);

        assertNotNull(jokeResource);
        assertEquals(jokeRequest.getDate(), jokeResource.getDate().toString());
        assertEquals(jokeRequest.getJoke(), jokeResource.getJoke());
        assertEquals(jokeRequest.getDescription(), jokeResource.getDescription().get());
    }

    @Test
    public void toResource_whenGivenModelWithNoDescription_shouldReturnWithEmptyDescription() {
        JokeOfTheDay jokeModel = JokeOfTheDay.builder()
                                        .id(UUID.randomUUID())
                                        .date(LocalDate.parse(DATE_FIELD))
                                        .joke(JOKE_FIELD)
                                        .description(null)
                                        .build();

        JokeOfTheDayResource jokeResource = JokeResourceMapper.toResource(jokeModel);

        assertNotNull(jokeResource);
        assertFalse(jokeResource.getDescription().isPresent());
    }

    @Test
    public void toResource_whenGivenRequestWithNoDescription_shouldReturnWithEmptyDescription() {
        JokeOfTheDayRequest jokeRequest = new JokeOfTheDayRequest(DATE_FIELD, JOKE_FIELD);

        JokeOfTheDayResource jokeResource = JokeResourceMapper.toResource(jokeRequest);

        assertNotNull(jokeResource);
        assertFalse(jokeResource.getDescription().isPresent());
    }
}
