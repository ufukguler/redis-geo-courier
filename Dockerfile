FROM maven:3.8.3-openjdk-17 AS build
COPY src /app/src
COPY pom.xml /app/pom.xml

RUN mvn -f /app/pom.xml clean install

FROM eclipse-temurin:17-jre-alpine

COPY --from=build /app/target/*.jar /app/app.jar

WORKDIR /app

EXPOSE 8080

ENTRYPOINT ["java","-jar","/app/app.jar","--spring.profiles.active=prod"]