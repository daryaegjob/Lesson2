package taskmanager;

/**
 * Выбрасывается, когда файл с задачами не удалось прочитать или разобрать
 * (плохой формат, битый JSON, несоответствие CSV-заголовку и т.д.).
 */
public class FileParsingException extends Exception {
    public FileParsingException(String message) {
        super(message);
    }
}
