# Use an official OpenJDK 21 runtime as a parent image
FROM eclipse-temurin:21-jdk-jammy

# Set the working directory in the container
WORKDIR /app

# Copy repo
COPY . /app

# Package the application
RUN ./mvnw package -DskipTests

# Copy the jar file to the app directory
RUN cp target/instagram-0.0.1-SNAPSHOT.jar app.jar

# Expose port 8080
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]
