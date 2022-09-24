# grpc-microservice
A dummy microservice system using grpc to communicate within services. Use Docker Compose to deploy these services

There are two microservices
1. user-service
    - do the user authentication and provide a jwt token
2. order-service
    - save orders and give saved orders
    - to use these services user need to be authenticated
    - so initially user need to be logged in using login url and get the jwt token
    - send the jwt token with consecutive requests

Using Grpc to communicate between these two services


Docker Compose
----------------------
- Using docker to put these services (and needed mysql dbs) into containers and use Docker Compose to connect these containers together
- Use `docker compose up` to start the services
- Use `docker compose down` to stop the services
- Use `docker scale <service> = n to scale up/down containers
  - `docker compose scale user-service=n` will create `n` number of user-service containers
  - `docker compose scale order-service=n` will create `n` number of order-service containers


Design
--------------

![image](https://user-images.githubusercontent.com/13161714/192107233-57b498b7-6ee0-476f-a88d-6f7bd876bbde.png)
