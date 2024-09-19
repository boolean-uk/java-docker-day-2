FROM eclipse-temurin:18

WORKDIR /app

COPY ../build/libs/java.docker.day.2-0.0.1.jar /app/java.docker.day.2-0.0.1.jar

EXPOSE 4000

ENTRYPOINT ["java", "-jar", "/app/java.docker.day.2-0.0.1.jar"]

# docker build -t my-docker-app -f Dockers/Dockerfile .