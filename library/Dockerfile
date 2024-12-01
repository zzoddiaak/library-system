FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/library-service.jar /app/library-service.jar

ENTRYPOINT ["java", "-jar", "library-service.jar"]

EXPOSE 8081
