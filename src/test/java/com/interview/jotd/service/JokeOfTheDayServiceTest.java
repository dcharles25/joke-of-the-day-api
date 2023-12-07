package com.interview.jotd.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.interview.jotd.data.JokeCollection;
import com.interview.jotd.data.JokeOfTheDay;
import com.interview.jotd.data.JokeOfTheDayResource;
import com.interview.jotd.data.exception.JokeDateConflictException;
import com.interview.jotd.repository.JokeOfTheDayDAO;
import com.interview.jotd.service.impl.JokeOfTheDayServiceImpl;

@ExtendWith(MockitoExtension.class)
public class JokeOfTheDayServiceTest {

    private JokeOfTheDayService<JokeOfTheDayResource> jokeOfTheDayService;

    @Mock
    private JokeOfTheDayDAO<JokeOfTheDay> jokeOfTheDayDAO;

    private JokeOfTheDay jokeModel;

    private JokeOfTheDayResource jokeResource;

    @BeforeEach
    public void init() {
        jokeOfTheDayService = new JokeOfTheDayServiceImpl(jokeOfTheDayDAO); 

        jokeModel = JokeOfTheDay.builder()
                        .id(UUID.randomUUID())
                        .date(LocalDate.now())
                        .joke("We never joke about unit tests")
                        .description("A test joke description")
                        .build();

        jokeResource = JokeOfTheDayResource.builder()
                            .date(LocalDate.now())
                            .joke("Don't even think about joking about unit tests")
                            .description(Optional.empty())
                            .build();
    }
    

    @Test
    public void getJokeCollection_whenDataReturned_shouldPopulateCollection() {
        List<JokeOfTheDay> modelList = new ArrayList<>();
        modelList.add(jokeModel);
        
        when(jokeOfTheDayDAO.getAll()).thenReturn(modelList);

        JokeCollection<JokeOfTheDayResource> collection = jokeOfTheDayService.getJokeCollection();

        assertNotNull(collection);
        assertFalse(collection.getCollection().isEmpty());
        assertEquals(1, collection.getTotal());
    }

    @Test
    public void getJokeCollection_whenNoDataReturned_shouldReturnEmptyCollection() {      
        when(jokeOfTheDayDAO.getAll()).thenReturn(Collections.emptyList());

        JokeCollection<JokeOfTheDayResource> collection = jokeOfTheDayService.getJokeCollection();

        assertNotNull(collection);
        assertTrue(collection.getCollection().isEmpty());
        assertEquals(0, collection.getTotal());

    }

    @Test
    public void getJokeById_whenJokeFound_shouldReturnJokeResource() {  
        UUID id = jokeModel.getId();    
        when(jokeOfTheDayDAO.getById(id)).thenReturn(Optional.of(jokeModel));

        Optional<JokeOfTheDayResource> returnedJoke = jokeOfTheDayService.getJokeById(id);

        assertTrue(returnedJoke.isPresent());

        JokeOfTheDayResource resource = returnedJoke.get();
        assertEquals(jokeModel.getId(), resource.getId());
        assertEquals(jokeModel.getDate(), resource.getDate());
        assertEquals(jokeModel.getJoke(), resource.getJoke());
        assertEquals(jokeModel.getDescription(), resource.getDescription().get());
    }

    @Test
    public void getJokeById_whenJokeFound_shouldReturnEmptyOptional() {  
        UUID id = jokeModel.getId();    
        when(jokeOfTheDayDAO.getById(id)).thenReturn(Optional.empty());

        Optional<JokeOfTheDayResource> returnedJoke = jokeOfTheDayService.getJokeById(id);

        assertFalse(returnedJoke.isPresent());
    }

    @Test
    public void getJokeByDate_whenJokeFound_shouldReturnJokeResource() {  
        LocalDate date = jokeModel.getDate();    
        when(jokeOfTheDayDAO.getByDate(date)).thenReturn(Optional.of(jokeModel));

        Optional<JokeOfTheDayResource> returnedJoke = jokeOfTheDayService.getJokeByDate(date);

        assertTrue(returnedJoke.isPresent());

        JokeOfTheDayResource resource = returnedJoke.get();
        assertEquals(jokeModel.getId(), resource.getId());
        assertEquals(jokeModel.getDate(), resource.getDate());
        assertEquals(jokeModel.getJoke(), resource.getJoke());
        assertEquals(jokeModel.getDescription(), resource.getDescription().get());
    }

    @Test
    public void getJokeByDate_whenJokeFound_shouldReturnEmptyOptional() {  
        LocalDate date = jokeModel.getDate();    
        when(jokeOfTheDayDAO.getByDate(date)).thenReturn(Optional.empty());

        Optional<JokeOfTheDayResource> returnedJoke = jokeOfTheDayService.getJokeByDate(date);

        assertFalse(returnedJoke.isPresent());
    }

    @Test
    public void createJoke_whenNoDateConflict_shouldCreateNewResource() {
        when(jokeOfTheDayDAO.getByDate(jokeResource.getDate())).thenReturn(Optional.empty());
        when(jokeOfTheDayDAO.create(any())).thenReturn(Optional.of(jokeModel));

        Optional<JokeOfTheDayResource> newJoke = jokeOfTheDayService.createJoke(jokeResource);

        assertTrue(newJoke.isPresent());

        JokeOfTheDayResource resource = newJoke.get();
        assertEquals(jokeModel.getId(), resource.getId());
        assertEquals(jokeModel.getDate(), resource.getDate());
        assertEquals(jokeModel.getJoke(), resource.getJoke());
        assertEquals(jokeModel.getDescription(), resource.getDescription().get());
    }

    @Test
    public void createJoke_whenDateConflict_shouldThrowConflictException() {
        when(jokeOfTheDayDAO.getByDate(jokeResource.getDate())).thenReturn(Optional.of(jokeModel));

        assertThrows(JokeDateConflictException.class, () -> {
            Optional<JokeOfTheDayResource> newJoke = jokeOfTheDayService.createJoke(jokeResource);
        });
    }

    @Test
    public void createJoke_whenNoJokeReturned_shouldReturnEmptyOptional() {
        when(jokeOfTheDayDAO.getByDate(jokeResource.getDate())).thenReturn(Optional.empty());
        when(jokeOfTheDayDAO.create(any())).thenReturn(Optional.empty());

        Optional<JokeOfTheDayResource> newJoke = jokeOfTheDayService.createJoke(jokeResource);

        assertFalse(newJoke.isPresent());
    }

    @Test
    public void updateOrCreateJoke_whenNoConflictOnDateChange_shouldReturnResource() {
        UUID id = jokeModel.getId();

        when(jokeOfTheDayDAO.getByDate(jokeResource.getDate())).thenReturn(Optional.empty());
        when(jokeOfTheDayDAO.replaceOrCreate(eq(id), any())).thenReturn(Optional.of(jokeModel));

        Optional<JokeOfTheDayResource> updatedJoke = jokeOfTheDayService.updateOrCreateJoke(id, jokeResource);

        assertTrue(updatedJoke.isPresent());

        JokeOfTheDayResource resource = updatedJoke.get();
        assertEquals(jokeModel.getId(), resource.getId());
        assertEquals(jokeModel.getDate(), resource.getDate());
        assertEquals(jokeModel.getJoke(), resource.getJoke());
        assertEquals(jokeModel.getDescription(), resource.getDescription().get());
    }

    @Test
    public void updateOrCreateJoke_whenConflictOnDateChange_shouldThrowConflictException() {
        UUID id = UUID.randomUUID();

        when(jokeOfTheDayDAO.getByDate(jokeResource.getDate())).thenReturn(Optional.of(jokeModel));

        assertThrows(JokeDateConflictException.class, () -> {
            Optional<JokeOfTheDayResource> updatedJoke = jokeOfTheDayService.updateOrCreateJoke(id, jokeResource);
        });
    }

    @Test
    public void updateOrCreateJoke_whenNoDateChange_shouldReturnResource() {
        UUID id = jokeModel.getId();

        when(jokeOfTheDayDAO.getByDate(jokeResource.getDate())).thenReturn(Optional.of(jokeModel));
        when(jokeOfTheDayDAO.replaceOrCreate(eq(id), any())).thenReturn(Optional.of(jokeModel));

        Optional<JokeOfTheDayResource> updatedJoke = jokeOfTheDayService.updateOrCreateJoke(id, jokeResource);

        assertTrue(updatedJoke.isPresent());

        JokeOfTheDayResource resource = updatedJoke.get();
        assertEquals(jokeModel.getId(), resource.getId());
        assertEquals(jokeModel.getDate(), resource.getDate());
        assertEquals(jokeModel.getJoke(), resource.getJoke());
        assertEquals(jokeModel.getDescription(), resource.getDescription().get());
    }

    @Test
    public void updateOrCreateJoke_whenNoDataReturned_shouldReturnEmptyOptional() {
        UUID id = jokeModel.getId();

        when(jokeOfTheDayDAO.getByDate(jokeResource.getDate())).thenReturn(Optional.of(jokeModel));
        when(jokeOfTheDayDAO.replaceOrCreate(eq(id), any())).thenReturn(Optional.empty());

        Optional<JokeOfTheDayResource> updatedJoke = jokeOfTheDayService.updateOrCreateJoke(id, jokeResource);

        assertFalse(updatedJoke.isPresent());
    }
}
