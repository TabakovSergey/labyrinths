# Инструкция по запуску проекта "Лабиринт"

## Требования

- **Java**: версия 24 или выше
- **Maven**: версия 3.9.11 или выше
- **Операционная система**: Windows, Linux, macOS
- **Docker** (опционально): для контейнеризации приложения

## Установка зависимостей

Проект использует Maven для управления зависимостями. Все зависимости автоматически загружаются при первой сборке.

## Сборка проекта

### Сборка через Maven

Откройте терминал в корневой директории проекта и выполните:

```bash
mvn clean compile
```

Для полной сборки с тестами:

```bash
mvn clean package
```

После успешной сборки JAR файл будет находиться в директории `target/` с именем `project-1.0.jar`.

## Запуск приложения

### Способ 1: Запуск через Maven

```bash
mvn exec:java -Dexec.mainClass="academy.Application" -Dexec.args="[аргументы]"
```

### Способ 2: Запуск скомпилированного JAR

```bash
java -jar target/project-1.0.jar [аргументы]
```

### Способ 3: Запуск через Spring Boot (если используется)

```bash
mvn spring-boot:run -Dspring-boot.run.arguments="[аргументы]"
```

## Использование приложения

### Просмотр справки

```bash
java -jar target/project-1.0.jar --help
```

или для конкретной команды:

```bash
java -jar target/project-1.0.jar generate --help
java -jar target/project-1.0.jar solve --help
```

### Генерация лабиринта

#### Базовый пример (генерация лабиринта 10x10 алгоритмом DFS):

```bash
java -jar target/project-1.0.jar generate --width 10 --height 10 --algorithm dfs --output maze.txt
```

#### Генерация лабиринта алгоритмом Prim:

```bash
java -jar target/project-1.0.jar generate --width 15 --height 15 --algorithm prim --output maze_prim.txt
```

#### Генерация с графическим отображением:

```bash
java -jar target/project-1.0.jar generate --width 20 --height 20 --algorithm dfs --window
```

#### Параметры команды generate:
- `--width` / `-w` (обязательный): ширина лабиринта в ячейках
- `--height` / `-h` (обязательный): высота лабиринта в ячейках
- `--algorithm` / `-a` (обязательный): алгоритм генерации (`dfs` или `prim`)
- `--output` / `-o` (опциональный): путь к файлу для сохранения лабиринта
- `--window` (опциональный): отобразить лабиринт в графическом окне

### Решение лабиринта

#### Базовый пример (решение лабиринта алгоритмом A*):

```bash
java -jar target/project-1.0.jar solve --algorithm astar --file maze.txt --start 0,0 --end 19,19 --output solution.txt
```

#### Решение алгоритмом Dijkstra:

```bash
java -jar target/project-1.0.jar solve --algorithm dijkstra --file maze.txt --start 0,0 --end 19,19 --output solution_dijkstra.txt
```

#### Решение с графическим отображением:

```bash
java -jar target/project-1.0.jar solve --algorithm astar --file maze.txt --start 0,0 --end 19,19 --window
```

#### Параметры команды solve:
- `--algorithm` / `-a` (обязательный): алгоритм решения (`dijkstra` или `astar`)
- `--file` / `-f` (обязательный): путь к файлу с лабиринтом
- `--start` (обязательный): начальная точка в формате `x,y`
- `--end` (обязательный): конечная точка в формате `x,y`
- `--output` / `-o` (опциональный): путь к файлу для сохранения решения
- `--window` (опциональный): отобразить лабиринт с решением в графическом окне

## Формат файла лабиринта

Лабиринт сохраняется в текстовом файле со следующими обозначениями:

- `#` - стена (непроходимая ячейка)
- ` ` (пробел) - проходимая ячейка
- `.` - точка пути (при сохранении с решением)
- `O` - начальная точка (при сохранении с решением)
- `X` - конечная точка (при сохранении с решением)

### Пример файла лабиринта:

```
#####################
#O#   #  ...........#
#.### # #.#########.#
#...#   #.#       #.#
###.#####.# ### # #.#
#...#...#.#   # # #.#
#.###.#.#.### # # #.#
#.#  .#.#...# # # #.#
#.###.#.###.# # ###.#
#.....#.....# #  X..# 
#####################
```

## Примеры использования

### Полный цикл работы:

1. **Генерация лабиринта:**
```bash
java -jar target/project-1.0.jar generate --width 20 --height 20 --algorithm dfs --output my_maze.txt
```

2. **Решение лабиринта:**
```bash
java -jar target/project-1.0.jar solve --algorithm astar --file my_maze.txt --start 1,1 --end 39,39 --output solution.txt
```

3. **Просмотр результата:**
Откройте файл `solution.txt` в текстовом редакторе или используйте графический режим:
```bash
java -jar target/project-1.0.jar solve --algorithm astar --file my_maze.txt --start 1,1 --end 39,39 --window
```

## Запуск тестов

### Запуск всех тестов:

```bash
mvn test
```

### Запуск тестов с отчетом о покрытии:

```bash
mvn test jacoco:report
```

Отчет будет находиться в `target/site/jacoco/index.html`

### Запуск интеграционных тестов:

```bash
mvn verify
```

## Проверка качества кода

### Запуск PMD (проверка стиля кода):

```bash
mvn pmd:check
```

### Запуск SpotBugs (поиск багов):

```bash
mvn spotbugs:check
```

### Форматирование кода:

```bash
mvn spotless:apply
```

## Устранение неполадок

### Проблема: "Java version mismatch"

**Решение:** Убедитесь, что используется Java 24 или выше:
```bash
java -version
```

Если версия ниже, установите Java 24 или обновите переменную окружения `JAVA_HOME`.

### Проблема: "Maven not found"

**Решение:** Убедитесь, что Maven установлен и доступен в PATH:
```bash
mvn -version
```

### Проблема: "File not found" при загрузке лабиринта

**Решение:** Убедитесь, что:
- Файл существует по указанному пути
- Путь указан правильно (используйте абсолютный путь или путь относительно текущей директории)
- Файл имеет правильный формат

### Проблема: "Start or end point is out of maze bounds"

**Решение:** Убедитесь, что координаты точек находятся в пределах лабиринта. Координаты начинаются с 0,0. Максимальные координаты зависят от размера лабиринта.

### Проблема: "Start or end point is inside a wall"

**Решение:** Начальная и конечная точки должны находиться на проходимых ячейках (пробелы), а не на стенах (#).

### Проблема: Графическое окно не открывается

**Решение:** 
- Убедитесь, что используется флаг `--window`
- Проверьте, что система поддерживает графический интерфейс (не сервер без GUI)
- Убедитесь, что установлены необходимые библиотеки для Swing

## Структура проекта

```
labyrinths/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── academy/
│   │   │       ├── Application.java          # Главный класс приложения
│   │   │       ├── AppConfig.java            # Конфигурация
│   │   │       └── maze/
│   │   │           ├── dto/                  # Data Transfer Objects
│   │   │           ├── Generate/             # Генераторы лабиринтов
│   │   │           ├── Solve/                 # Решатели лабиринтов
│   │   │           ├── IO/                    # Загрузка/сохранение
│   │   │           ├── View/                  # Визуализация
│   │   │           └── HelpAlgorithm/         # Вспомогательные алгоритмы
│   │   └── resources/                        # Ресурсы
│   └── test/
│       └── java/                              # Тесты
├── pom.xml                                    # Maven конфигурация
├── README.md                                  # Основной README
└── INSTRUCTIONS.md                            # Эта инструкция
```

## Запуск через Docker

### Быстрый старт с Docker

```bash
# Сборка образа
docker build -t maze-app:latest .

# Генерация лабиринта
docker run --rm -v $(pwd)/mazes:/app/mazes maze-app:latest \
  generate --width 10 --height 10 --algorithm dfs --output /app/mazes/maze.txt

# Решение лабиринта
docker run --rm -v $(pwd)/mazes:/app/mazes maze-app:latest \
  solve --algorithm astar --file /app/mazes/maze.txt --start 1,1 --end 19,19 --output /app/mazes/solution.txt
```

### Использование Docker Compose

```bash
# Запуск контейнера
docker-compose up -d

# Выполнение команд
docker-compose exec maze-app java -jar app.jar generate --width 10 --height 10 --algorithm dfs --output /app/mazes/maze.txt
```

**Подробная документация по Docker:** см. [DOCKER.md](./DOCKER.md)

## CI/CD

Проект настроен для автоматической сборки и тестирования:

- **GitLab CI**: автоматически запускается при push/merge request (см. `.gitlab-ci.yml`)
- **GitHub Actions**: автоматически запускается при push/PR (см. `.github/workflows/ci-cd.yml`)

Pipeline включает:
- Проверку качества кода (Spotless, PMD, SpotBugs)
- Запуск тестов
- Сборку JAR файла
- Сборку Docker образа
- Интеграционные тесты в Docker

**Подробная документация по CI/CD:** см. [CI_CD.md](./CI_CD.md)

## Дополнительная информация

- Для получения подробной информации об алгоритмах см. комментарии в коде (Doxygen)
- Все классы имеют полную документацию
- Примеры тестовых случаев находятся в директории `tests/cases/`
- Docker инструкции: [DOCKER.md](./DOCKER.md)
- CI/CD настройка: [CI_CD.md](./CI_CD.md)

## Контакты и поддержка

При возникновении проблем проверьте:
1. Версию Java и Maven
2. Корректность путей к файлам
3. Формат входных данных
4. Логи приложения (если включены)

