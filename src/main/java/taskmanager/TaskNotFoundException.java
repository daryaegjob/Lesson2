package taskmanager;

/**
 * Выбрасывается, когда задача с указанным id не найдена.
 */
public class TaskNotFoundException extends Exception {
    public TaskNotFoundException(int id) {
        super("Задача с id=" + id + " не найдена");
    }
}
