/**
 * Таблица: описание структуры (имена колонок) + сами записи.
 * Row[] по порядку значений соответствует columnNames.
 */
public record Table(String[] columnNames, Row[] rows) {
}
