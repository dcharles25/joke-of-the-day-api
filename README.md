# Joke Of The Day REST API

## Description

A REST API that allows clients to perform CRUD operations on a Joke of the Day resource. 

## Getting Started

### Dependencies

* This project requires Docker.

### Building and Deploying 

* Once docker has been installed, the application can be built and deployed using the provided docker-compose file
* From a command line prompt, navigate to the root of this project and run the following:
```
docker-compose build
docker-compose up -d
```

* After running these commands, you should have two runnning containers - the api and the database. The following command can be used to verify. 
```
docker-compose ps
```

### Playing with the API

By default, the API can be reached at http://localhost:8080/api/joke-of-the-day

The API supports the following REST calls:

```
GET /api/joke-of-the-day/collection
GET /api/joke-of-the-day?date={date}
GET /api/joke-of-the-day/{id}
POST /api/joke-of-the-day
PUT /api/joke-of-the-day/{id}
DELETE /api/joke-of-the-day/{id}
```

The API consumes and produces json. Below you'll find an example body of a POST request to get things started. 

```
{
    "date": "2023-12-18",
    "joke": "What happens to a frog's car when it breaks down? It gets toad away.",
    "description": "Just a good old fashioned toad joke."
}
```
