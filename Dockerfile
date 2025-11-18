# Multi-stage build для оптимизации размера образа
# Этап 1: Сборка приложения
FROM maven:3.9.11-eclipse-temurin-24 AS build

WORKDIR /build

# Копируем pom.xml и загружаем зависимости (кэширование слоев)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Копируем исходный код и собираем проект
COPY src ./src
RUN mvn clean package -DskipTests -B

# Этап 2: Финальный образ
ARG RUNTIME_IMAGE=eclipse-temurin:24-jre
FROM ${RUNTIME_IMAGE}

WORKDIR /app

# Создаем непривилегированного пользователя для безопасности
RUN groupadd -r appuser && useradd -r -g appuser appuser

# Копируем JAR из этапа сборки
COPY --from=build /build/target/project-1.0.jar app.jar

# Меняем владельца файлов
RUN chown -R appuser:appuser /app

# Переключаемся на непривилегированного пользователя
USER appuser

# Точка входа
ENTRYPOINT ["java", "-jar", "app.jar"]

# Метаданные
LABEL maintainer="Academy Team"
LABEL description="Maze Generator and Solver Application"
LABEL version="1.0"
