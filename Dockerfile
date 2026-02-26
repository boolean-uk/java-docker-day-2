FROM mcr.microsoft.com/openjdk/jdk:21-ubuntu

WORKDIR /app

COPY /build/libs/java.docker.day.2-0.0.1-SNAPSHOT.jar /app/java.docker.day.2-0.0.1-SNAPSHOT.jar

EXPOSE 4000

ENTRYPOINT [ "java", "-jar", "java.docker.day.2-0.0.1-SNAPSHOT.jar" ]
