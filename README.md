# Currency Exchange Homework for SEB


## Installation:
You need to have docker and docker-compose installed on your machine.

1. Clone this repo.
2. Clone https://github.com/krsmll/currency-exchange-angular.
3. Create this file in the same directory you cloned these repos to.
```yaml
version: '3.8'

services:
  backend:
    build: ./currency-exchange/services/exchange
    ports:
      - "8080:8080"

  frontend:
    build: ./currency-exchange-angular
    ports:
      - "4200:4200"
```
4. Run `docker-compose up`

That's it. Backend is running on port 8080 and frontend on port 4200.

Access them at `http://localhost:8080` and `http://localhost:4200` respectively.

## Swagger:
Swagger is available at `/api/v1/swagger-ui/index.html`

## Running tests:
You need to have maven installed on your machine.
```mvn clean test```
or
```mvn test```


## What could be improved:
- Add more tests
- Add logging
- Use Kubernetes, Helm for deployment but it's an overkill for a project of this size

