FROM openjdk:17-jdk-slim

# Install Maven
RUN apt-get update && apt-get install -y maven

WORKDIR /app

# Copy everything
COPY . .

# Build the application
RUN mvn clean package -DskipTests

# Find and copy the jar file
RUN find target -name "*.jar" -exec cp {} app.jar \;

# Expose the port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]




#FROM openjdk:17-jdk-slim
#
#WORKDIR /app
#
## Copy the Maven wrapper and pom.xml first (for better caching)
#COPY mvnw .
#COPY .mvn .mvn
#COPY pom.xml .
#
## Download dependencies (this layer will be cached)
#RUN ./mvnw dependency:go-offline -B
#
## Copy source code
#COPY src src
#
## Build the application
#RUN ./mvnw clean package -DskipTests
#
## Expose the port
#EXPOSE 8080
#
## Run the application
#ENTRYPOINT ["java", "-jar", "target/*.jar"]
#


## Stage 1: Build the jar
#FROM maven:3.9.6-eclipse-temurin-17 as builder
#WORKDIR /app
#COPY . .
#RUN mvn clean package -DskipTests
#
## Stage 2: Run the jar
#FROM openjdk:17-jdk-slim
#WORKDIR /app
#COPY --from=builder /app/target/*.jar app.jar
#EXPOSE 8080
#ENTRYPOINT ["java", "-jar", "app.jar"]
#





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
