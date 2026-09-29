package taskmanager;

/**
 * Таблица задач: id | title | description | status.
 */
public class TaskDatabase extends AbstractDatabase<Task> {

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
            Integer.parseInt(columns[0]);
        } catch (NumberFormatException e) {
            throw new ValidationException("Поле id должно быть числом: " + columns[0]);
        }
        if (columns[1] == null || columns[1].isBlank()) {
            throw new ValidationException("Поле title не может быть пустым");
        }
    }

    @Override
    protected Row toRow(Task task) {
        return new Row(new String[]{
                String.valueOf(task.getId()),
                task.getTitle(),
                task.getDescription(),
                task.getStatus()
        });
    }

    @Override
    protected Task fromRow(Row row) {
        String[] c = row.columns();
        return new Task(Integer.parseInt(c[0]), c[1], c[2], c[3]);
    }

    @Override
    protected int extractId(Row row) {
        return Integer.parseInt(row.columns()[0]);
    }
}
