# Currency Exchange Homework for SEB

### Author: Kristjan Mill

## Stack:
- Monorepo for backend services
- Java 17
- Angular 17
- Spring Boot
- H2
- Docker
- Docker Compose
- JUnit
- Wiremock
- Mockito
- Swagger
- Lombok
- Spotless


## Installation:
You need to have docker and docker-compose installed on your machine.
> docker-compose up

That's it. Backend is running on port 8080 and frontend on port 4200.

Access them at `http://localhost:8080` and `http://localhost:4200` respectively.

## Swagger:
Swagger is available at `http://localhost:8080/api/v1/swagger-ui/index.html`

## Running tests:
You need to have maven installed on your machine.
> mvn clean test

or
> mvn test


## What could be improved:
- Add more tests
- Add logging
- Use Kubernetes, Helm for deployment but it's an overkill for a project of this size

