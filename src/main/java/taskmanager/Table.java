package taskmanager;

import java.util.List;

/**
 * Таблица: описание структуры (имена колонок) + сами записи.
 * Записи хранятся в List<Row> (ArrayList) — коллекция сама растёт.
 */
public record Table(String[] columnNames, List<Row> rows) {
}
