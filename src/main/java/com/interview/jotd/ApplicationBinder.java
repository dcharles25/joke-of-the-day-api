package com.interview.jotd;

import javax.ws.rs.ext.Provider;

import org.glassfish.hk2.api.TypeLiteral;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

import com.interview.jotd.data.JokeOfTheDay;
import com.interview.jotd.data.JokeOfTheDayResource;
import com.interview.jotd.repository.JokeOfTheDayDAO;
import com.interview.jotd.repository.impl.JokeOfTheDayDAOImpl;
import com.interview.jotd.service.JokeOfTheDayService;
import com.interview.jotd.service.impl.JokeOfTheDayServiceImpl;

@Provider
public class ApplicationBinder extends AbstractBinder{

    @Override
    protected void configure() {
        bind(JokeOfTheDayServiceImpl.class).to(new TypeLiteral<JokeOfTheDayService<JokeOfTheDayResource>>() {});
        bind(JokeOfTheDayDAOImpl.class).to(new TypeLiteral<JokeOfTheDayDAO<JokeOfTheDay>>() {});
    }
    
}
