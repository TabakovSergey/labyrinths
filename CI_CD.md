# CI/CD Pipeline документация

## Обзор

Проект настроен с автоматизированными CI/CD pipeline для:
- Проверки качества кода
- Автоматического тестирования
- Сборки приложения
- Создания Docker образов
- Публикации артефактов

## Поддерживаемые платформы

### GitLab CI

Файл конфигурации: `.gitlab-ci.yml`

**Стадии:**
1. **validate** - Проверка качества кода и тестирование
   - `Run Linter` - проверка форматирования, PMD, SpotBugs
   - `Run Test` - запуск unit-тестов
2. **build** - Сборка приложения
   - `Build` - сборка JAR файла
   - `Build Docker Image` - создание Docker образа
3. **blackbox-tests** - Интеграционные тесты
   - `Run Black Box Tests` - тестирование в Docker контейнере

**Триггеры:**
- Push в защищенные ветки
- Merge Request события

### GitHub Actions

Файл конфигурации: `.github/workflows/ci-cd.yml`

**Jobs:**
1. **lint** - Проверка качества кода
   - Spotless (форматирование)
   - Modernizer
   - SpotBugs
   - PMD

2. **build-and-test** - Сборка и тестирование
   - Компиляция проекта
   - Запуск тестов
   - Генерация отчета о покрытии
   - Сборка JAR файла
   - Загрузка артефактов

3. **build-docker** - Сборка Docker образа
   - Сборка приложения
   - Создание Docker образа
   - Сохранение образа как артефакт

4. **docker-tests** - Тестирование Docker образа
   - Проверка запуска контейнера
   - Тестирование функциональности
   - Очистка

5. **publish-docker** - Публикация образа (только main ветка)
   - Публикация в Docker Hub
   - Тегирование версий

**Триггеры:**
- Push в `main` или `develop`
- Pull Request в `main` или `develop`
- Ручной запуск (workflow_dispatch)

## Настройка CI/CD

### GitLab CI

Настройка уже выполнена в `.gitlab-ci.yml`. Pipeline автоматически запускается при:
- Push в репозиторий
- Создании Merge Request

**Дополнительные настройки:**
- Кэширование Maven зависимостей
- Использование Docker-in-Docker для сборки образов
- Интеграция с GitLab Container Registry (если настроено)

### GitHub Actions

#### Базовая настройка

1. Убедитесь, что файл `.github/workflows/ci-cd.yml` присутствует в репозитории
2. Pipeline автоматически запустится при push/PR

#### Настройка публикации Docker образа

Для публикации образов в Docker Hub:

1. Создайте аккаунт на [Docker Hub](https://hub.docker.com/)
2. Перейдите в Settings → Secrets → Actions вашего GitHub репозитория
3. Добавьте следующие secrets:
   - `DOCKER_USERNAME` - ваш Docker Hub username
   - `DOCKER_PASSWORD` - ваш Docker Hub password или access token

После настройки образы будут автоматически публиковаться при push в `main` ветку.

**Теги образов:**
- `latest` - последняя версия из main ветки
- `{commit-sha}` - версия для конкретного коммита

#### Использование Codecov (опционально)

Для загрузки отчетов о покрытии кода:

1. Зарегистрируйтесь на [Codecov](https://codecov.io/)
2. Добавьте репозиторий
3. Получите токен (если требуется)
4. Добавьте secret `CODECOV_TOKEN` в GitHub (если используется приватный репозиторий)

## Локальный запуск CI/CD

### Запуск проверок локально

```bash
# Проверка форматирования
./mvnw spotless:check

# Исправление форматирования
./mvnw spotless:apply

# Проверка PMD
./mvnw pmd:check

# Проверка SpotBugs
./mvnw spotbugs:check

# Запуск тестов
./mvnw test

# Сборка
./mvnw clean package
```

### Тестирование Docker сборки локально

```bash
# Сборка образа
docker build -t maze-app:test .

# Тестирование
docker run --rm maze-app:test --help
docker run --rm -v $(pwd)/test-output:/app/output maze-app:test \
  generate --width 5 --height 5 --algorithm dfs --output /app/output/test.txt
```

## Мониторинг Pipeline

### GitLab

1. Перейдите в **CI/CD → Pipelines**
2. Просмотрите статус каждого pipeline
3. Кликните на pipeline для деталей
4. Просмотрите логи каждого job

### GitHub

1. Перейдите в **Actions** вкладку репозитория
2. Выберите workflow run
3. Просмотрите детали каждого job
4. Скачайте артефакты (если доступны)

## Устранение неполадок

### Pipeline не запускается

**GitLab:**
- Проверьте, что файл `.gitlab-ci.yml` находится в корне репозитория
- Убедитесь, что CI/CD включен в настройках проекта

**GitHub:**
- Проверьте, что файл `.github/workflows/ci-cd.yml` существует
- Убедитесь, что workflow файл имеет правильный синтаксис YAML

### Тесты падают

1. Запустите тесты локально: `./mvnw test`
2. Проверьте логи в CI/CD интерфейсе
3. Убедитесь, что все зависимости доступны

### Docker сборка не работает

1. Проверьте, что Dockerfile корректен
2. Убедитесь, что JAR файл собирается успешно
3. Проверьте логи сборки Docker образа

### Проблемы с кэшированием

**Очистка кэша Maven:**
```bash
./mvnw dependency:purge-local-repository
```

**Очистка кэша Docker:**
```bash
docker system prune -a
```

## Оптимизация Pipeline

### Ускорение сборки

1. **Кэширование зависимостей Maven**
   - Уже настроено в обоих CI/CD системах
   - Зависимости кэшируются между запусками

2. **Параллельное выполнение**
   - Jobs выполняются параллельно где возможно
   - Используйте `needs` для зависимостей

3. **Условное выполнение**
   - Некоторые jobs выполняются только при определенных условиях
   - Например, публикация Docker образа только для main ветки

### Сокращение времени выполнения

- Используйте `-DskipTests` для быстрой сборки (когда тесты уже пройдены)
- Кэшируйте Docker слои
- Используйте matrix builds для параллельного тестирования на разных версиях Java (если нужно)

## Интеграция с Jira

Для автоматического обновления статусов задач в Jira:

1. Настройте Jira integration в GitLab/GitHub
2. Используйте номера задач в commit messages: `[PROJ-123] Описание`
3. Статусы будут обновляться автоматически при merge

## Дополнительные возможности

### Автоматическое развертывание

Можно расширить pipeline для автоматического развертывания:
- Deploy в staging при merge в develop
- Deploy в production при merge в main
- Использование Kubernetes для оркестрации

### Уведомления

Настройте уведомления о статусе pipeline:
- Email уведомления
- Slack интеграция
- Telegram боты

## Полезные ссылки

- [GitLab CI/CD документация](https://docs.gitlab.com/ee/ci/)
- [GitHub Actions документация](https://docs.github.com/en/actions)
- [Docker best practices](https://docs.docker.com/develop/dev-best-practices/)
- [Maven best practices](https://maven.apache.org/guides/index.html)

