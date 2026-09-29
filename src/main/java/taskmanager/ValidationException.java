package taskmanager;

/**
 * Выбрасывается, когда запись не соответствует структуре/правилам таблицы.
 */
public class ValidationException extends Exception {
    public ValidationException(String message) {
        super(message);
    }
}
