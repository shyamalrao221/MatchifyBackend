# Stage 1: Use Maven with Java 21 to build the Spring Boot application
FROM maven:3.9.9-eclipse-temurin-21 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy all project files from local machine into the container
COPY . .

# Build the application JAR file and skip running tests
RUN ./mvnw clean package -DskipTests

# Stage 2: Use a smaller Java 21 runtime image to run the app
FROM eclipse-temurin:21-jre

# Set the working directory for the runtime container
WORKDIR /app

# Copy the generated JAR file from the build stage into the runtime container
COPY --from=build /app/target/*.jar app.jar

# Expose port 8080 because the Spring Boot app runs on this port
EXPOSE 8080

# Start the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
