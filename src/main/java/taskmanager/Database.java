package taskmanager;

import java.util.List;

/**
 * Контракт "базы данных" для одной таблицы, дженерик по типу записи T.
 */
public interface Database<T> {
    void insert(T item) throws ValidationException;
    boolean deleteById(int id);
    List<T> findAll();
    T findById(int id);
    List<T> findWhere(String columnName, String valuePart);
    String[] getColumnNames();
}
