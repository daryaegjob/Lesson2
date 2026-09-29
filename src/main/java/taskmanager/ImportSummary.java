package taskmanager;

/**
 * Итог группового импорта: сколько задач успешно сохранено из скольких было в файле.
 */
public record ImportSummary(int importedCount, int totalCount) {
}
