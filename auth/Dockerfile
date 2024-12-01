# Используем официальный образ для Java
FROM openjdk:17-jdk-slim

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем файлы скомпилированного приложения в контейнер
COPY target/book-service.jar /app/book-service.jar

# Запускаем приложение
ENTRYPOINT ["java", "-jar", "book-service.jar"]

# Открываем порт
EXPOSE 8080
