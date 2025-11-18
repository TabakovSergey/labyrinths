package academy;

/**
 * Конфигурация приложения.
 *
 * <p>Содержит настройки приложения, такие как размер шрифта и список слов.
 *
 * @param fontSize размер шрифта
 * @param words массив слов
 */
public record AppConfig(int fontSize, String[] words) {}
