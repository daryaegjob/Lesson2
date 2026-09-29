import java.util.List;

/**
 * Управляет задачами. Работает с Database<Task> — дженерик уже зафиксирован
 * конкретным типом записи (Task), поэтому наружу отдаются типизированные
 * List<Task>, а не сырые Row или массивы.
 */
public class TaskManager {

    private final Database<Task> database;
    private int nextId = 1;

    public TaskManager(Database<Task> database) {
        this.database = database;
    }

    public Task addTask(String title, String description, String status) throws ValidationException {
        Task task = new Task(nextId, title, description, status);
        database.insert(task);
        nextId++;
        return task;
    }

    public void removeTask(int id) throws TaskNotFoundException {
        boolean removed = database.deleteById(id);
        if (!removed) {
            throw new TaskNotFoundException(id);
        }
    }

    /** Ищет задачи по вхождению текста в название (регистронезависимо). */
    public List<Task> findTaskByTitle(String title) {
        return database.findWhere("title", title);
    }

    public Task findTaskById(int id) throws TaskNotFoundException {
        Task task = database.findById(id);
        if (task == null) {
            throw new TaskNotFoundException(id);
        }
        return task;
    }

    /**
     * Обновление задачи по id. Любой параметр можно передать как null —
     * тогда соответствующее поле не меняется.
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

        database.deleteById(id);
        database.insert(updated);
    }

    /** Печатает список всех задач в консоль. */
    public void getListTasks() {
        List<Task> tasks = database.findAll();
        if (tasks.isEmpty()) {
            System.out.println("Список задач пуст.");
            return;
        }
        System.out.println("Список задач:");
        for (Task task : tasks) {
            System.out.println(task);
        }
    }
}
