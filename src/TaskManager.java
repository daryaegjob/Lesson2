/**
 * Управляет задачами. Не знает, что БД внутри реализована через массив records —
 * работает только через интерфейс Database. Отвечает за перевод Task <-> Row.
 */
public class TaskManager {

    private final Database database;
    private int nextId = 1;

    public TaskManager(Database database) {
        this.database = database;
    }

    public Task addTask(String title, String description, String status) throws ValidationException {
        Task task = new Task(nextId, title, description, status);
        database.insert(task.toRow());
        nextId++;
        return task;
    }

    public void removeTask(int id) throws TaskNotFoundException {
        boolean removed = database.deleteWhere("id", String.valueOf(id));
        if (!removed) {
            throw new TaskNotFoundException(id);
        }
    }

    /** Ищет задачи по вхождению текста в название (регистронезависимо). */
    public Task[] findTaskByTitle(String title) {
        Row[] rows = database.selectAll();
        String needle = title.toLowerCase();

        int count = 0;
        for (Row row : rows) {
            if (row.columns()[1].toLowerCase().contains(needle)) {
                count++;
            }
        }

        Task[] result = new Task[count];
        int index = 0;
        for (Row row : rows) {
            if (row.columns()[1].toLowerCase().contains(needle)) {
                result[index++] = Task.fromRow(row);
            }
        }
        return result;
    }

    public Task findTaskById(int id) throws TaskNotFoundException {
        Row[] rows = database.select("id", String.valueOf(id));
        if (rows.length == 0) {
            throw new TaskNotFoundException(id);
        }
        return Task.fromRow(rows[0]);
    }

    /**
     * Бонус сверх минимального задания: обновление задачи по id.
     * Row/Table неизменяемые, поэтому обновление = удалить старую строку + вставить новую.
     * Любой параметр можно передать как null — тогда соответствующее поле не меняется.
     */
    public void updateTask(int id, String newTitle, String newDescription, String newStatus)
            throws TaskNotFoundException, ValidationException {
        Task existing = findTaskById(id);

        Task updated = new Task(
                id,
                newTitle != null ? newTitle : existing.getTitle(),
                newDescription != null ? newDescription : existing.getDescription(),
                newStatus != null ? newStatus : existing.getStatus()
        );

        database.deleteWhere("id", String.valueOf(id));
        database.insert(updated.toRow());
    }

    /** Печатает список всех задач в консоль. */
    public void getListTasks() {
        Row[] rows = database.selectAll();
        if (rows.length == 0) {
            System.out.println("Список задач пуст.");
            return;
        }
        System.out.println("Список задач:");
        for (Row row : rows) {
            System.out.println(Task.fromRow(row));
        }
    }
}
