FROM openjdk:17-jdk-slim

# Install curl and ffmpeg inside container
RUN apt-get update && apt-get install -y curl ffmpeg

WORKDIR /app

COPY target/sampleGnec_1-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]

