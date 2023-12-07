package com.interview.jotd.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.jvnet.hk2.annotations.Contract;

@Contract
public interface JokeOfTheDayDAO<T> {

    List<T> getAll();

    Optional<T> getById(UUID id);
    
    Optional<T> getByDate(LocalDate date);
    
    Optional<T> create(T t);

    Optional<T> replaceOrCreate(UUID id, T t);

    void delete(UUID id); 
}
