# Use OpenJDK base image
FROM azul/zulu-openjdk:21 as build

# Set working directory
WORKDIR /app

# Copy the Gradle build files
COPY build/libs/store-*.jar app.jar

# Expose the port your application runs on (adjust if needed)
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]