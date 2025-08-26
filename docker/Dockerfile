FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app

COPY java.docker.day.2-0.0.2-SNAPSHOT.jar /app/app.jar

EXPOSE 4000

ENTRYPOINT ["java", "-jar", "app.jar"]
