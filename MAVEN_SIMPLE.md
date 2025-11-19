# Самый простой Maven сценарий

## Требования
- Java 24+
- Maven 3.9+

## Сборка без лишних плагинов
```bash
mvn -q clean package -DskipTests
```

## Сборка с тестами (рекомендуется)
```bash
mvn -q clean verify
```

## Шаги
1. Установи Java и Maven.
2. В корне проекта запусти `mvn -q clean verify`.
3. Готовый jar появится в `target/project-1.0.jar`.

## Полезные команды
```bash
mvn -q test           # только тесты
mvn -q package        # сборка без тестов (если были прогнаны)
mvn -q dependency:go-offline  # скачать зависимости заранее
```

## Чистка
```bash
mvn -q clean
```

