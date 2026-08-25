/**
 * Выбрасывается, когда запись не соответствует структуре/правилам таблицы.
 * Сделано checked-исключением намеренно: вызывающий код обязан либо обработать
 * его (try/catch), либо явно пробросить дальше (throws) — это и есть тема занятия.
 */
public class ValidationException extends Exception {
    public ValidationException(String message) {
        super(message);
    }
}
