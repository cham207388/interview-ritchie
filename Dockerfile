# Use a lightweight JDK 21 base image
FROM eclipse-temurin:21-jdk-alpine AS build

# Set working directory in the container
WORKDIR /app

# Copy Gradle wrapper files and other dependencies
COPY gradlew settings.gradle build.gradle /app/
COPY gradle /app/gradle/

# Download dependencies to speed up builds
RUN ./gradlew dependencies --no-daemon

# Copy the project source code
COPY src /app/src

# Build the Spring Boot application
RUN ./gradlew build --no-daemon -x test

# Second stage: runtime image
FROM eclipse-temurin:21-jre-alpine

RUN addgroup -S -g 1000 appuser && \
    adduser -S -u 1000 -G appuser appuser && \
    mkdir -p /app && \
    chown appuser:appuser /app

USER appuser

# Set working directory
WORKDIR /app

# Copy the Spring Boot JAR file from the build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Expose the application port
EXPOSE 8080

# Run the Spring Boot application
CMD ["java", "-jar", "app.jar"]