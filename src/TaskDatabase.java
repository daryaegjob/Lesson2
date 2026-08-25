/**
 * Таблица задач: id | title | description | status.
 * Знает, какие данные считаются корректными для этой конкретной таблицы.
 */
public class TaskDatabase extends AbstractDatabase {

    public static final String[] COLUMNS = {"id", "title", "description", "status"};

    public TaskDatabase() {
        super(COLUMNS);
    }

    @Override
    protected void validateStructure(Row row) throws ValidationException {
        String[] columns = row.columns();

        if (columns.length != COLUMNS.length) {
            throw new ValidationException(
                    "Ожидается " + COLUMNS.length + " колонок, получено " + columns.length);
        }

        try {
            Integer.parseInt(columns[0]); // id
        } catch (NumberFormatException e) {
            throw new ValidationException("Поле id должно быть числом: " + columns[0]);
        }

        if (columns[1] == null || columns[1].isBlank()) { // title
            throw new ValidationException("Поле title не может быть пустым");
        }
    }
}
