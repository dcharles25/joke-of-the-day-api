package com.interview.jotd.service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import org.jvnet.hk2.annotations.Contract;

import com.interview.jotd.data.JokeCollection;

@Contract
public interface JokeOfTheDayService<T> {

    public JokeCollection<T> getJokeCollection();

    public Optional<T> getJokeById(UUID id);

    public Optional<T> getJokeByDate(LocalDate date);

    public Optional<T> createJoke(T newJoke);

    public Optional<T> updateOrCreateJoke(UUID id, T updatedJoke);

    public void deleteJoke(UUID id);
    
}
