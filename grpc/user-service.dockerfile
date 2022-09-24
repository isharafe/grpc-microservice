FROM maven:3.8.6-jdk-11-slim AS user-service-build
WORKDIR /app
copy . .
RUN mvn package -q

FROM adoptopenjdk/openjdk11:x86_64-alpine-jre-11.0.5_10
copy --from=user-service-build /app/user/target/user-0.0.1-SNAPSHOT.jar /app/user-service.jar
ENTRYPOINT ["java","-jar","/app/user-service.jar"]
