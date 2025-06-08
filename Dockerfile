FROM openjdk:17-jdk-slim

# Install Maven
RUN apt-get update && apt-get install -y maven

WORKDIR /app
COPY . .

# Build the application
RUN mvn clean package -DskipTests

# Find and rename the jar file
RUN mv target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]