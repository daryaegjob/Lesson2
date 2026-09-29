package taskmanager;

import java.util.List;

/**
 * Управляет задачами. Работает с Database<Task>.
 */
public class TaskManager {

    private final Database<Task> database;
    private int nextId = 1;

    public TaskManager(Database<Task> database) {
        this.database = database;
    }

    /** Создаёт новую задачу с автогенерируемым id — для команд из консоли. */
    public Task addTask(String title, String description, String status) throws ValidationException {
        Task task = new Task(nextId, title, description, status);
        database.insert(task);
        nextId++;
        return task;
    }

    /**
     * Кладёт в БД уже готовую задачу с собственным id — используется при импорте
     * из файла, где id уже назначены. Подтягивает nextId, чтобы не было коллизий
     * с задачами, которые потом добавят вручную через addTask.
     */
    public Task importTask(Task task) throws ValidationException {
        database.insert(task);
        if (task.getId() >= nextId) {
            nextId = task.getId() + 1;
        }
        return task;
    }

    /**
     * Групповое сохранение в БД: пытается импортировать каждую задачу,
     * невалидные — пропускает (с сообщением в консоль), не прерывая остальные.
     * Побочный эффект (вставка в БД) внутри filter — не самый "чистый" функциональный
     * стиль, но для операции массового импорта это обычный прагматичный компромисс.
     */
    public ImportSummary importAll(List<Task> tasks) {
        long importedCount = tasks.stream()
                .filter(this::tryImport)
                .count();
        return new ImportSummary((int) importedCount, tasks.size());
    }

    private boolean tryImport(Task task) {
        try {
            importTask(task);
            return true;
        } catch (ValidationException e) {
            System.out.println("Пропущена задача #" + task.getId() + ": " + e.getMessage());
            return false;
        }
    }

    public void removeTask(int id) throws TaskNotFoundException {
        boolean removed = database.deleteById(id);
        if (!removed) {
            throw new TaskNotFoundException(id);
        }
    }

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
