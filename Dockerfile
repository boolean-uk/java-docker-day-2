FROM openjdk:17-jdk-slim

WORKDIR /app

COPY build/libs/*.jar app.jar

EXPOSE 4000

CMD ["java", "-jar", "app.jar"]