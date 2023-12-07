package com.interview.jotd.api;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import java.util.UUID;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.interview.jotd.data.JokeCollection;
import com.interview.jotd.data.JokeOfTheDayRequest;
import com.interview.jotd.data.JokeOfTheDayResource;
import com.interview.jotd.data.mapper.JokeResourceMapper;
import com.interview.jotd.service.JokeOfTheDayService;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Path("joke-of-the-day") 
public class JokeOfTheDayAPI {

    private JokeOfTheDayService<JokeOfTheDayResource> jokeOfTheDayService;

    @Inject
    public JokeOfTheDayAPI(JokeOfTheDayService<JokeOfTheDayResource> jokeOfTheDayService) {
        this.jokeOfTheDayService = jokeOfTheDayService;
    }
    

    @GET
    @Path("/collection")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getJokeCollection() {
        log.info("Processing request to retrieve a collection of all jokes of the day");
    
        JokeCollection<JokeOfTheDayResource> jokeCollection = jokeOfTheDayService.getJokeCollection();

        return Response.ok(jokeCollection).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getJokeByDate(@QueryParam("date") String date) {
        log.info("Processing request to retrieve the joke of the day for provided date {}", date);

        if (date == null) {
            throw new BadRequestException("Date must not be blank");
        }

        try {
            Optional<JokeOfTheDayResource> joke = jokeOfTheDayService.getJokeByDate(LocalDate.parse(date));

            if (joke.isPresent()) {
                return Response.ok(joke.get()).build();
            } else {
                throw new NotFoundException("Unable to locate joke of the day for date - " + date);
            }    
        } catch (DateTimeParseException e) {
            throw new BadRequestException("Date parameter must be in a valid format, e.g. yyyy-MM-dd");
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getJokeById(@PathParam("id") UUID id) {
        log.info("Processing request to retrieve the joke of the day associated with id {}", id.toString());
        
        Optional<JokeOfTheDayResource> joke = jokeOfTheDayService.getJokeById(id);

        if (joke.isPresent()) {
            return Response.ok(joke.get()).build();
        } else {
            throw new NotFoundException("Unable to locate joke of the day associated with id - " + id.toString());
        }    
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createJoke(@Valid JokeOfTheDayRequest newJokeRequest) {
        log.info("Processing request to create a new joke of the day for {}", newJokeRequest.getDate());

        JokeOfTheDayResource jokeResource = JokeResourceMapper.toResource(newJokeRequest);
        Optional<JokeOfTheDayResource> newJoke = jokeOfTheDayService.createJoke(jokeResource);
        
        if (newJoke.isPresent()) {
            return Response.status(Status.CREATED).entity(newJoke.get()).build();   
        } else {
            throw new InternalServerErrorException("An unknown error has occurred while processing request to create a joke of the day for date " + newJokeRequest.getDate());
        }
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateJoke(@PathParam("id") UUID id, @Valid JokeOfTheDayRequest updateJokeRequest) {
        log.info("Processing request to update the joke of the day associated with id {}", id);

        JokeOfTheDayResource jokeResource = JokeResourceMapper.toResource(updateJokeRequest);
        Optional<JokeOfTheDayResource> updatedJoke = jokeOfTheDayService.updateOrCreateJoke(id, jokeResource);

        if (updatedJoke.isPresent()) {
            return Response.status(Status.CREATED).entity(updatedJoke.get()).build();   
        } else {
            throw new InternalServerErrorException("An unknown error has occurred while processing request to update the joke of the day with id " + id);
        }
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteJoke(@PathParam("id") UUID id) {
        log.info("Processing request to delete the joke of the day associated with id {}", id);
        
        jokeOfTheDayService.deleteJoke(id);
        
        return Response.noContent().build();
    }
}
