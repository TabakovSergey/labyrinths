# Git Workflow и интеграция с Jira

## Работа с ветками

### Рекомендуемая структура веток

```
main/master          # Основная ветка (защищена, только через MR/PR)
├── develop          # Ветка разработки
    ├── feature/PROJ-123-task-name    # Ветка для задачи
    ├── feature/PROJ-124-task-name    # Ветка для другой задачи
    └── hotfix/PROJ-125-bug-fix       # Срочные исправления
```

### Workflow для команды

#### Вариант 1: Каждый в своей ветке (рекомендуется)

```bash
# 1. Обновить develop
git checkout develop
git pull origin develop

# 2. Создать ветку для задачи (используйте номер задачи из Jira)
git checkout -b feature/PROJ-123-implement-dfs-generator

# 3. Работа над задачей
# ... пишем код, добавляем Doxygen комментарии ...

# 4. Коммиты с номером задачи в сообщении
git add .
git commit -m "[PROJ-123] Реализован генератор DFS с Doxygen документацией"
git commit -m "[PROJ-123] Добавлены unit-тесты для генератора"

# 5. Push ветки
git push origin feature/PROJ-123-implement-dfs-generator

# 6. Создать Merge Request в GitLab / Pull Request в GitHub
# При создании MR/PR укажите номер задачи: "Closes PROJ-123"
```

#### Вариант 2: Работа в общей ветке (для парной работы)

```bash
# 1. Обновить develop
git checkout develop
git pull origin develop

# 2. Создать общую ветку для спринта
git checkout -b sprint-1-week-1

# 3. Оба разработчика работают в этой ветке
# Важно: часто делать pull перед push
git pull origin sprint-1-week-1  # перед началом работы
# ... работа ...
git add .
git commit -m "[PROJ-123] Описание изменений"
git push origin sprint-1-week-1
```

## Формат commit messages для Jira

### Базовый формат:
```
[PROJ-XXX] Краткое описание изменений
```

### Примеры:
```
[PROJ-123] Реализован генератор DFS
[PROJ-123] Добавлены Doxygen комментарии к GenerationDFS
[PROJ-124] Реализован решатель Dijkstra
[PROJ-125] Добавлена загрузка лабиринтов из файла
[PROJ-126] Исправлена ошибка валидации координат
```

### Для закрытия задачи:
```
[PROJ-123] Реализован генератор DFS

Closes PROJ-123
```

Или в описании Merge Request:
```
Closes PROJ-123
Fixes PROJ-124
```

## Интеграция Jira с GitLab

### Настройка в GitLab:

1. **Settings → Integrations → Jira**
2. Ввести:
   - **Jira URL**: `https://your-company.atlassian.net`
   - **Email**: ваш email в Jira
   - **API Token**: получить на https://id.atlassian.com/manage-profile/security/api-tokens
3. Сохранить

### Автоматическое обновление статусов:

- При коммите с `[PROJ-123]` - задача переходит в "In Progress"
- При создании MR с `Closes PROJ-123` - задача переходит в "Review"
- При merge MR - задача переходит в "Done"

## Интеграция Jira с GitHub

### Настройка через Atlassian:

1. **Jira → Settings → Applications → DVCS accounts**
2. Нажать "Link GitHub account"
3. Авторизоваться через GitHub
4. Выбрать репозиторий
5. Настроить автоматические переходы

### Или через GitHub App:

1. **GitHub → Settings → Integrations → Jira**
2. Установить Jira app
3. Подключить к Jira проекту

## Работа в команде

### Планирование спринта:

1. **В начале спринта:**
   - Открыть `SPRINTS_AND_TASKS.md`
   - Создать задачи в Jira
   - Назначить исполнителей
   - Переместить задачи в спринт

2. **Распределение задач:**
   - Каждый разработчик берет задачи из спринта
   - Создает ветку для каждой задачи
   - Работает независимо

### Ежедневная работа:

```bash
# Утро: обновить develop
git checkout develop
git pull origin develop

# Создать/переключиться на ветку задачи
git checkout feature/PROJ-123-task-name
# или
git checkout -b feature/PROJ-123-task-name

# Работа...
git add .
git commit -m "[PROJ-123] Описание"
git push origin feature/PROJ-123-task-name

# Вечер: обновить ветку
git pull origin develop  # получить изменения от партнера
```

### Code Review:

1. Создать Merge Request в GitLab/GitHub
2. Указать номер задачи: `Closes PROJ-123`
3. Попросить партнера провести review
4. После одобрения - merge
5. Статус задачи в Jira обновится автоматически

## Решение конфликтов

```bash
# Если возник конфликт при merge
git checkout develop
git pull origin develop
git checkout feature/PROJ-123-task-name
git merge develop

# Решить конфликты вручную
# ... редактирование файлов ...

git add .
git commit -m "[PROJ-123] Разрешен конфликт с develop"
git push origin feature/PROJ-123-task-name
```

## Полезные команды

```bash
# Просмотр веток
git branch -a

# Просмотр коммитов с номерами задач
git log --grep="PROJ-"

# Просмотр изменений
git diff

# Отмена локальных изменений
git checkout -- .

# Сброс к последнему коммиту
git reset --hard HEAD

# Просмотр статуса
git status
```

## Чеклист перед коммитом

- [ ] Код компилируется без ошибок
- [ ] Тесты проходят
- [ ] Добавлены Doxygen комментарии
- [ ] Commit message содержит номер задачи: `[PROJ-XXX]`
- [ ] Изменения соответствуют задаче
- [ ] Нет лишних файлов (target/, .idea/, etc.)

## Чеклист перед Merge Request

- [ ] Все задачи выполнены
- [ ] Код прошел code review
- [ ] CI/CD pipeline успешно выполнен
- [ ] Тесты проходят
- [ ] Doxygen комментарии добавлены
- [ ] В описании MR указан номер задачи: `Closes PROJ-XXX`

