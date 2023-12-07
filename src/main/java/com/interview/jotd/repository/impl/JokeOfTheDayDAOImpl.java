package com.interview.jotd.repository.impl;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.bson.Document;
import org.bson.UuidRepresentation;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.jvnet.hk2.annotations.Service;

import com.interview.jotd.data.JokeOfTheDay;
import com.interview.jotd.data.exception.JokeDataAccessException;
import com.interview.jotd.repository.JokeOfTheDayDAO;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class JokeOfTheDayDAOImpl implements JokeOfTheDayDAO<JokeOfTheDay> {

    private static final String DB_URL = System.getenv("DB_URL");
    private static final String DB_NAME = System.getenv("DB_NAME");
    private static final String COLLECTION_NAME = System.getenv("DB_COLLECTION_NAME");

    private MongoClientSettings clientSettings;


    public JokeOfTheDayDAOImpl() {
        if (DB_URL == null || DB_NAME == null || COLLECTION_NAME == null) {
            throw new IllegalStateException("Failed to initialize database connection variables. Check environemnt variables.");
        }

        ConnectionString connectionString = new ConnectionString(DB_URL);
        CodecRegistry pojoCodecRegistry = fromProviders(PojoCodecProvider.builder()
                                                                .automatic(true)
                                                                .build());

        CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);

        clientSettings = MongoClientSettings.builder()
                                    .uuidRepresentation(UuidRepresentation.STANDARD)
                                    .applyConnectionString(connectionString)
                                    .codecRegistry(codecRegistry)
                                    .build();
    }

    @Override
    public List<JokeOfTheDay> getAll() {
        try (MongoClient client = MongoClients.create(clientSettings)) { 
            List<JokeOfTheDay> jokesList = new ArrayList<>();

            MongoCollection<JokeOfTheDay> jokesCollection = getDatabaseCollection(client, DB_NAME, COLLECTION_NAME, JokeOfTheDay.class);

            jokesCollection.find()
                    .forEach(item -> jokesList.add(item));  

            return jokesList;
        } catch (MongoException e) {
            String message = "An error has occurred while attempted to retrieve all joke of the day records.";
            log.error(message, e);
            throw new JokeDataAccessException(message, e);
        }     
    }

    @Override
    public Optional<JokeOfTheDay> getById(UUID id) {
        try (MongoClient client = MongoClients.create(clientSettings)) { 
            
            MongoCollection<JokeOfTheDay> jokesCollection = getDatabaseCollection(client, DB_NAME, COLLECTION_NAME, JokeOfTheDay.class);

            Document findById = new Document("_id", id);

            return Optional.ofNullable(jokesCollection.find(findById).first());
        } catch (MongoException e) {
            String message = "An error has occurred while attempted to retrieve a joke of the day record by id";
            log.error(message + "- id: {}", id.toString(), e);
            throw new JokeDataAccessException(message, id.toString(), e);
        }
    }

    @Override
    public Optional<JokeOfTheDay> getByDate(LocalDate date) {
        try (MongoClient client = MongoClients.create(clientSettings)) { 
            
            MongoCollection<JokeOfTheDay> jokesCollection = getDatabaseCollection(client, DB_NAME, COLLECTION_NAME, JokeOfTheDay.class);

            Document findById = new Document("date", date);

            return Optional.ofNullable(jokesCollection.find(findById).first());
        } catch (MongoException e) {
            String message = "An error has occurred while attempted to retrieve a joke of the day by date";
            log.error(message + "- date: {}", date.toString(), e);
            throw new JokeDataAccessException(message, date.toString(), e);
           
        }
    }

    @Override
    public Optional<JokeOfTheDay> create(JokeOfTheDay newJoke) {
         try (MongoClient client = MongoClients.create(clientSettings)) { 
            
            MongoCollection<JokeOfTheDay> jokesCollection = getDatabaseCollection(client, DB_NAME, COLLECTION_NAME, JokeOfTheDay.class);

            newJoke.setId(generateId());
            InsertOneResult result = jokesCollection.insertOne(newJoke);

            Document findById = new Document("_id", result.getInsertedId());

            return Optional.ofNullable(jokesCollection.find(findById).first());
        } catch (MongoException e) {
            String message = "An error has occurred while attempted to insert a new joke of the day for a given date";
            log.error(message + "- date: {}", newJoke.getDate().toString(), e);
            throw new JokeDataAccessException(message, newJoke.getDate().toString(), e);
        }
    }

    @Override
    public Optional<JokeOfTheDay> replaceOrCreate(UUID id, JokeOfTheDay updatedJoke) {
        try (MongoClient client = MongoClients.create(clientSettings)) { 
    
            MongoCollection<JokeOfTheDay> jokesCollection = getDatabaseCollection(client, DB_NAME, COLLECTION_NAME, JokeOfTheDay.class);

            Document findByIdQuery = new Document().append("_id", id);
            
            //If no resource with given id exist, create one
            ReplaceOptions options = new ReplaceOptions().upsert(true);

            updatedJoke.setId(id);
            UpdateResult result = jokesCollection.replaceOne(findByIdQuery, updatedJoke, options);

            if (result.getUpsertedId() != null) {
                log.info("A new joke was created during a replace operation. Joke associated with id {}", id);
            }
            
            Document findById = new Document("_id", id);

            return Optional.ofNullable(jokesCollection.find(findById).first());
        } catch (MongoException e) {
            String message = "An error has occurred while attempted to replace the joke of the day for a given id";
            log.error(message + "- id: {}", id.toString(), e);
            throw new JokeDataAccessException(message, id.toString(), e);
        }
    }

    @Override
    public void delete(UUID id) {
        try (MongoClient client = MongoClients.create(clientSettings)) { 
            
            MongoCollection<JokeOfTheDay> jokesCollection = getDatabaseCollection(client, DB_NAME, COLLECTION_NAME, JokeOfTheDay.class);

            Document findByIdQuery = new Document().append("_id", id);

            jokesCollection.deleteOne(findByIdQuery);
        } catch (MongoException e) {
            String message = "An error has occurred while attempted to delete the joke of the day for a given id";
            log.error(message + "- id: {}", id.toString(), e);
            throw new JokeDataAccessException(message, id.toString(), e);
        }
    }


    private <T> MongoCollection<T> getDatabaseCollection(MongoClient client, String dbName, String collectionName, Class<T> collectionClass) {
        MongoDatabase database = client.getDatabase(DB_NAME);
        MongoCollection<T> collection = database.getCollection(collectionName, collectionClass);

        return collection;
    }

    private UUID generateId() {
        return UUID.randomUUID();
    }
}
