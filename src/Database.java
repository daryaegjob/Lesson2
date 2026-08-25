/**
 * Контракт "базы данных" на одну таблицу.
 * Интерфейс ничего не знает про задачи конкретно — только про строки/колонки.
 * Это позволяет TaskManager-у работать с абстракцией, не зная, что происходит внутри.
 */
public interface Database {

    /** Добавляет запись, предварительно проверив её структуру/корректность. */
    void insert(Row row) throws ValidationException;

    /** Удаляет первую найденную запись, где columnName == value. Возвращает true, если что-то удалено. */
    boolean deleteWhere(String columnName, String value);

    /** Возвращает все записи, где columnName == value (точное совпадение). */
    Row[] select(String columnName, String value);

    /** Возвращает все записи таблицы. */
    Row[] selectAll();

    /** Возвращает имена колонок таблицы. */
    String[] getColumnNames();
}
