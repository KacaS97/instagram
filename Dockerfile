# Use an official OpenJDK 21 runtime as a parent image
FROM openjdk:21-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the project files to the container
COPY target/instagram.jar /app/instagram.jar

# Package the application
RUN ./mvnw package -DskipTests

# Copy the jar file to the app directory
COPY target/instagram-0.0.1-SNAPSHOT.jar app.jar

# Expose port 8080
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]
