FROM eclipse-temurin:21

WORKDIR /app

COPY java.docker.day.2-0.0.1.jar /app/java.docker.day.2-0.0.1.jar

EXPOSE 4000

ENTRYPOINT ["java", "-jar", "/app/java.docker.day.2-0.0.1.jar"]