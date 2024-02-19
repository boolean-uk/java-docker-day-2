FROM eclipse-temurin:17
WORKDIR /app
COPY build/libs/java.docker.day.2-0.0.1.jar /app/java.docker.day.2-0.0.1.jar
EXPOSE 4000
ENTRYPOINT ["java", "-jar", "java.docker.day.2-0.0.1.jar"]
