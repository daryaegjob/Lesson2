import java.util.List;

/**
 * Таблица: описание структуры (имена колонок) + сами записи.
 * Записи хранятся в List<Row> (ArrayList) — коллекция сама растёт,
 * ручное управление размером массива, как в занятии 1, больше не нужно.
 */
public record Table(String[] columnNames, List<Row> rows) {
}
