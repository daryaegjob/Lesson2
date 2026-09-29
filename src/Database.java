import java.util.List;

/**
 * Контракт "базы данных" для одной таблицы, дженерик по типу записи T.
 * TaskManager будет работать с Database<Task> и не увидит внутри
 * ни массивов, ни сырых Row — только типизированные объекты T.
 */
public interface Database<T> {

    /** Добавляет запись, предварительно проверив её структуру/корректность. */
    void insert(T item) throws ValidationException;

    /** Удаляет запись с данным id. Возвращает true, если что-то удалено. */
    boolean deleteById(int id);

    /** Возвращает все записи таблицы. */
    List<T> findAll();

    /** Возвращает запись по id, или null, если не найдена. */
    T findById(int id);

    /** Возвращает записи, где значение колонки columnName содержит valuePart (без учёта регистра). */
    List<T> findWhere(String columnName, String valuePart);

    /** Возвращает имена колонок таблицы. */
    String[] getColumnNames();
}
