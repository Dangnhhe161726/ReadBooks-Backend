
FROM gradle:7.5-jdk17 AS build
WORKDIR /app
COPY . /app/backend
WORKDIR /app/backend
RUN gradle clean build -x test
FROM openjdk:17-slim
WORKDIR /app
COPY --from=build /app/build/libs/backend-0.0.1-SNAPSHOT-plain.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
