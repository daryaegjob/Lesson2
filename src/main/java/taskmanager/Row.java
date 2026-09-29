package taskmanager;

/**
 * Одна запись (строка) в таблице.
 * Значения хранятся строго в том же порядке, что и columnNames таблицы.
 */
public record Row(String[] columns) {
}
