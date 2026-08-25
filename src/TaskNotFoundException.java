/**
 * Выбрасывается, когда задача с указанным id не найдена
 * (при удалении, обновлении, поиске по id).
 */
public class TaskNotFoundException extends Exception {
    public TaskNotFoundException(int id) {
        super("Задача с id=" + id + " не найдена");
    }
}
