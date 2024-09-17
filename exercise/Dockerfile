FROM eclipse-temurin:17

WORKDIR /app
COPY docker-d2-0.0.1.jar /app/app.jar

EXPOSE 4000

ENTRYPOINT ["java", "-jar", "app.jar"]