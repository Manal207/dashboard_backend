# Stage 1: Build the jar
FROM maven:3.9.6-eclipse-temurin-17 as builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Run the jar
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]






## Use OpenJDK as base image
#FROM openjdk:17-jdk-slim
#
## Set the working directory inside the container
#WORKDIR /app
#
## Copy jar file to container
#COPY target/*.jar app.jar
#
## Expose the port your app runs on
#EXPOSE 8080
#
## Run the jar file
#ENTRYPOINT ["java", "-jar", "app.jar"]
