# Docker инструкции для проекта "Лабиринт"

## Обзор

Проект поддерживает контейнеризацию через Docker, что позволяет легко развертывать и запускать приложение в любой среде.

## Быстрый старт

### Сборка Docker образа

```bash
# Сборка образа
docker build -t maze-app:latest .

# Или с указанием runtime образа
docker build --build-arg RUNTIME_IMAGE=eclipse-temurin:24-jre -t maze-app:latest .
```

### Запуск контейнера

```bash
# Просмотр справки
docker run --rm maze-app:latest --help

# Генерация лабиринта
docker run --rm -v $(pwd)/mazes:/app/mazes maze-app:latest \
  generate --width 10 --height 10 --algorithm dfs --output /app/mazes/maze.txt

# Решение лабиринта
docker run --rm -v $(pwd)/mazes:/app/mazes maze-app:latest \
  solve --algorithm astar --file /app/mazes/maze.txt --start 1,1 --end 19,19 --output /app/mazes/solution.txt
```

**Для Windows (PowerShell):**
```powershell
# Генерация лабиринта
docker run --rm -v ${PWD}/mazes:/app/mazes maze-app:latest `
  generate --width 10 --height 10 --algorithm dfs --output /app/mazes/maze.txt
```

## Использование Docker Compose

### Производственный режим

```bash
# Запуск контейнера
docker-compose up -d

# Выполнение команд
docker-compose exec maze-app java -jar app.jar generate --width 10 --height 10 --algorithm dfs --output /app/mazes/maze.txt

# Просмотр логов
docker-compose logs -f maze-app

# Остановка
docker-compose down
```

### Режим разработки

```bash
# Запуск контейнера для разработки
docker-compose -f docker-compose.dev.yml up -d

# Вход в контейнер
docker-compose -f docker-compose.dev.yml exec maze-app-dev bash

# Внутри контейнера можно работать с Maven
mvn clean compile
mvn test
mvn package
```

## Структура Docker файлов

### Dockerfile (production)
- Multi-stage build для оптимизации размера
- Использует непривилегированного пользователя для безопасности
- Минимальный runtime образ

### Dockerfile.dev (development)
- Полный Maven образ с инструментами разработки
- Монтирование исходного кода для hot-reload
- Дополнительные утилиты (vim, git)

## Монтирование томов

Для сохранения входных/выходных файлов используйте volumes:

```bash
# Создайте директории для данных
mkdir -p mazes data

# Монтируйте при запуске
docker run --rm -v $(pwd)/mazes:/app/mazes -v $(pwd)/data:/app/data \
  maze-app:latest generate --width 10 --height 10 --algorithm dfs --output /app/mazes/maze.txt
```

## Переменные окружения

Можно настроить JVM параметры через переменные окружения:

```bash
docker run --rm -e JAVA_OPTS="-Xmx1g -Xms512m" \
  maze-app:latest generate --width 20 --height 20 --algorithm dfs
```

## Примеры использования

### Полный цикл работы в Docker

```bash
# 1. Генерация лабиринта
docker run --rm -v $(pwd)/mazes:/app/mazes maze-app:latest \
  generate --width 15 --height 15 --algorithm prim --output /app/mazes/my_maze.txt

# 2. Решение лабиринта
docker run --rm -v $(pwd)/mazes:/app/mazes maze-app:latest \
  solve --algorithm astar --file /app/mazes/my_maze.txt \
  --start 1,1 --end 29,29 --output /app/mazes/solution.txt

# 3. Просмотр результата
cat mazes/solution.txt
```

### Интерактивный режим

```bash
# Запуск контейнера в интерактивном режиме
docker run -it --rm -v $(pwd)/mazes:/app/mazes maze-app:latest bash

# Внутри контейнера
java -jar app.jar generate --width 10 --height 10 --algorithm dfs --output /app/mazes/test.txt
```

## Оптимизация образа

Текущий Dockerfile использует multi-stage build:
- **Этап сборки**: использует полный Maven образ
- **Этап runtime**: использует минимальный JRE образ

Это позволяет получить небольшой финальный образ (~200-300 MB) при сохранении всех возможностей сборки.

## Безопасность

- Контейнер запускается от непривилегированного пользователя `appuser`
- Минимальный набор зависимостей в runtime образе
- Нет установленных дополнительных пакетов в production образе

## Troubleshooting

### Проблема: "Permission denied" при записи файлов

**Решение:** Убедитесь, что директории для монтирования имеют правильные права:
```bash
mkdir -p mazes data
chmod 777 mazes data  # Для разработки
```

### Проблема: "Cannot connect to Docker daemon"

**Решение:** Убедитесь, что Docker daemon запущен:
```bash
# Linux
sudo systemctl start docker

# Windows/Mac
# Запустите Docker Desktop
```

### Проблема: Образ слишком большой

**Решение:** Используйте multi-stage build (уже реализовано) и очищайте кэш:
```bash
docker system prune -a
```

## CI/CD интеграция

Docker образ автоматически собирается в CI/CD pipeline:
- **GitLab CI**: см. `.gitlab-ci.yml`
- **GitHub Actions**: см. `.github/workflows/ci-cd.yml`

Образы публикуются в Docker Hub (при настройке secrets) или GitLab Container Registry.

