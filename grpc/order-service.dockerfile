FROM maven:3.8.6-jdk-11-slim AS order-service-build
WORKDIR /app
copy . .
RUN mvn package -q

FROM alpine:latest

RUN apk add openjdk11-jre
copy --from=order-service-build /app/order/target/order-0.0.1-SNAPSHOT.jar /app/order-service.jar
ENTRYPOINT ["java","-jar","/app/order-service.jar"]
