# Use an OpenJDK base image with Maven pre-installed
FROM maven:3.8.6-openjdk-17-slim as builder

# Set the working directory inside the container
WORKDIR /app

# Copy the pom.xml and src folder to the container
COPY pom.xml .
COPY src ./src

# Run Maven to build the project and create the JAR file
RUN mvn clean package -DskipTests

# Second stage: Use OpenJDK image to run the application
FROM openjdk:17-jdk-slim

# Install curl and ffmpeg inside the container
RUN apt-get update && apt-get install -y curl ffmpeg

# Set the working directory inside the container
WORKDIR /app

# Copy the jar from the builder stage to the current image
COPY --from=builder /app/target/sampleGnec_1-0.0.1-SNAPSHOT.jar app.jar

# Expose the port for the Spring Boot application
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]


