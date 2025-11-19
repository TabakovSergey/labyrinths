# Самый простой Dockerfile: ожидаем, что JAR уже собран (mvn package)
FROM eclipse-temurin:24-jre
WORKDIR /app
# Копируем уже собранный jar
COPY target/project-1.0.jar app.jar
# Минимальная точка входа
ENTRYPOINT ["java", "-jar", "app.jar"]
