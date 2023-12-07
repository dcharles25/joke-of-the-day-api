package com.interview.jotd.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.jvnet.hk2.annotations.Service;

import com.interview.jotd.data.JokeCollection;
import com.interview.jotd.data.JokeOfTheDay;
import com.interview.jotd.data.JokeOfTheDayResource;
import com.interview.jotd.data.exception.JokeDateConflictException;
import com.interview.jotd.data.mapper.JokeModelMapper;
import com.interview.jotd.data.mapper.JokeResourceMapper;
import com.interview.jotd.repository.JokeOfTheDayDAO;
import com.interview.jotd.service.JokeOfTheDayService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class JokeOfTheDayServiceImpl implements JokeOfTheDayService<JokeOfTheDayResource> {

    private JokeOfTheDayDAO<JokeOfTheDay> jokeOfTheDayDao;

    @Inject
    public JokeOfTheDayServiceImpl(JokeOfTheDayDAO<JokeOfTheDay> jokeOfTheDayDAO) {
        this.jokeOfTheDayDao = jokeOfTheDayDAO;
    }


    public JokeCollection<JokeOfTheDayResource> getJokeCollection() {

        List<JokeOfTheDayResource> jokes = jokeOfTheDayDao.getAll()
                                                    .stream()
                                                    .map(model -> JokeResourceMapper.toResource(model))
                                                    .collect(Collectors.toList());

        JokeCollection<JokeOfTheDayResource> jokeCollection = new JokeCollection<>(jokes, jokes.size());
        
        return jokeCollection;
    }

    public Optional<JokeOfTheDayResource> getJokeById(UUID id) {
    
        Optional<JokeOfTheDay> joke = jokeOfTheDayDao.getById(id);
        if (joke.isPresent()) {
            return joke.map(value -> JokeResourceMapper.toResource(value));
        } else {
            log.info("No joke of the day with id {} found", id.toString());
            return Optional.empty();
        }
    }

    public Optional<JokeOfTheDayResource> getJokeByDate(LocalDate date) {
        
        Optional<JokeOfTheDay> joke = jokeOfTheDayDao.getByDate(date);
        if (joke.isPresent()) {
            return joke.map(value -> JokeResourceMapper.toResource(value));
        } else {
            log.info("No joke of the day for date {} found", date.toString());
            return Optional.empty();
        }
    }

    public Optional<JokeOfTheDayResource> createJoke(JokeOfTheDayResource newJoke) {
        
        Optional<JokeOfTheDay> existingJoke = jokeOfTheDayDao.getByDate(newJoke.getDate());
        if (!existingJoke.isPresent()) {
            Optional<JokeOfTheDay> joke = jokeOfTheDayDao.create(
                                                JokeModelMapper.toModel(newJoke));

            if (joke.isPresent()) {
                return joke.map(value -> JokeResourceMapper.toResource(value));
            } else {
                return Optional.empty();
            }
        } else {
            log.error("Unable to create new joke of the day. A joke already exists for for provided date {}", newJoke.getDate().toString());
            throw new JokeDateConflictException("Unable to create new joke of the day. A joke already exists for for provided date", newJoke.getDate());
        }
    }

    public Optional<JokeOfTheDayResource> updateOrCreateJoke(UUID id, JokeOfTheDayResource updatedJoke) {
    
        Optional<JokeOfTheDay> existingJoke = jokeOfTheDayDao.getByDate(updatedJoke.getDate());
        if (!existingJoke.isPresent() || 
                        (existingJoke.isPresent() && id.equals(existingJoke.get().getId()))) {

            Optional<JokeOfTheDay> joke = jokeOfTheDayDao.replaceOrCreate(id, JokeModelMapper.toModel(updatedJoke));
            if (joke.isPresent()) {
                return joke.map(value -> JokeResourceMapper.toResource(value));
            } else {
                return Optional.empty();
            }
        } else {
            log.error("Unable to update the joke of the day {}. A joke already exists for provided joke's updated date {}", id, updatedJoke.getDate().toString());
            throw new JokeDateConflictException("Unable to update joke of the day. A joke already exists for provided joke's updated date", updatedJoke.getDate());
        }
    }

    public void deleteJoke(UUID id) {
        jokeOfTheDayDao.delete(id);
    }
}
