# Use the official OpenJDK base image
FROM eclipse-temurin:18

# Set the working directory in the container
WORKDIR /app

# Copy the packaged jar file into the container
COPY build/libs/app.jar /app/app.jar

# Expose the port the application runs on
EXPOSE 4000

# Run the jar file
CMD ["java", "-jar", "java.docker.day.1-0.1-plain.jar"]
