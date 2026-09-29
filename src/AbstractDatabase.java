import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Общая реализация Database<T> поверх Table (List<Row> внутри).
 *
 * Класс не знает, что такое T конкретно (Task или что-то ещё) — вся связь
 * между T и Row вынесена в абстрактные методы, которые реализует наследник
 * (например, TaskDatabase). Это тот же Template Method, что и в занятии 2,
 * только теперь параметризованный дженериком.
 */
public abstract class AbstractDatabase<T> implements Database<T> {

    private final Table table;

    protected AbstractDatabase(String[] columnNames) {
        this.table = new Table(columnNames, new ArrayList<>());
    }

    @Override
    public void insert(T item) throws ValidationException {
        Row row = toRow(item);
        validateStructure(row);
        table.rows().add(row); // коллекция сама увеличится при необходимости
    }

    @Override
    public boolean deleteById(int id) {
        // Iterator.remove() — безопасный способ удалять элементы во время обхода коллекции,
        // в отличие от обычного for-each, где List.remove() внутри цикла кидает исключение.
        Iterator<Row> iterator = table.rows().iterator();
        while (iterator.hasNext()) {
            Row row = iterator.next();
            if (extractId(row) == id) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }

    @Override
    public List<T> findAll() {
        List<T> result = new ArrayList<>();
        for (Row row : table.rows()) {
            result.add(fromRow(row));
        }
        return result;
    }

    @Override
    public T findById(int id) {
        for (Row row : table.rows()) {
            if (extractId(row) == id) {
                return fromRow(row);
            }
        }
        return null;
    }

    @Override
    public List<T> findWhere(String columnName, String valuePart) {
        int columnIndex = indexOfColumn(columnName);
        String needle = valuePart.toLowerCase();

        List<T> result = new ArrayList<>();
        for (Row row : table.rows()) {
            if (row.columns()[columnIndex].toLowerCase().contains(needle)) {
                result.add(fromRow(row));
            }
        }
        return result;
    }

    @Override
    public String[] getColumnNames() {
        return table.columnNames();
    }

    // ---- то, что должен реализовать конкретный наследник (знает про T) ----

    /** Проверка структуры/корректности строки перед вставкой. */
    protected abstract void validateStructure(Row row) throws ValidationException;

    /** Превращает объект T в строку таблицы (в порядке колонок). */
    protected abstract Row toRow(T item);

    /** Собирает объект T обратно из строки таблицы. */
    protected abstract T fromRow(Row row);

    /** Достаёт id из строки (для поиска/удаления по id). */
    protected abstract int extractId(Row row);

    protected int indexOfColumn(String columnName) {
        String[] names = table.columnNames();
        for (int i = 0; i < names.length; i++) {
            if (names[i].equals(columnName)) {
                return i;
            }
        }
        throw new IllegalArgumentException("Неизвестная колонка: " + columnName);
    }
}
