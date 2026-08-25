/**
 * Общая реализация Database поверх immutable Table/Row.
 * Так как record нельзя изменить "на месте", каждая мутация (insert/delete)
 * создаёт новый массив Row[] и новый объект Table, который заменяет старый.
 *
 * Абстрактный метод validateStructure() оставлен подклассам: общая механика
 * хранения одна для любой таблицы, а вот "что считается корректной записью" —
 * знание конкретной таблицы (например, TaskDatabase).
 */
public abstract class AbstractDatabase implements Database {

    private Table table;

    protected AbstractDatabase(String[] columnNames) {
        this.table = new Table(columnNames, new Row[0]);
    }

    @Override
    public void insert(Row row) throws ValidationException {
        validateStructure(row);

        Row[] oldRows = table.rows();
        Row[] newRows = new Row[oldRows.length + 1];
        System.arraycopy(oldRows, 0, newRows, 0, oldRows.length);
        newRows[oldRows.length] = row;

        table = new Table(table.columnNames(), newRows);
    }

    @Override
    public boolean deleteWhere(String columnName, String value) {
        int columnIndex = indexOfColumn(columnName);
        Row[] oldRows = table.rows();

        int matchIndex = -1;
        for (int i = 0; i < oldRows.length; i++) {
            if (oldRows[i].columns()[columnIndex].equals(value)) {
                matchIndex = i;
                break;
            }
        }
        if (matchIndex == -1) {
            return false;
        }

        Row[] newRows = new Row[oldRows.length - 1];
        System.arraycopy(oldRows, 0, newRows, 0, matchIndex);
        System.arraycopy(oldRows, matchIndex + 1, newRows, matchIndex, oldRows.length - matchIndex - 1);

        table = new Table(table.columnNames(), newRows);
        return true;
    }

    @Override
    public Row[] select(String columnName, String value) {
        int columnIndex = indexOfColumn(columnName);
        Row[] rows = table.rows();

        int count = 0;
        for (Row row : rows) {
            if (row.columns()[columnIndex].equals(value)) {
                count++;
            }
        }

        Row[] result = new Row[count];
        int index = 0;
        for (Row row : rows) {
            if (row.columns()[columnIndex].equals(value)) {
                result[index++] = row;
            }
        }
        return result;
    }

    @Override
    public Row[] selectAll() {
        return table.rows();
    }

    @Override
    public String[] getColumnNames() {
        return table.columnNames();
    }

    /**
     * Проверка структуры/корректности записи перед вставкой.
     * Каждая конкретная таблица определяет собственные правила.
     */
    protected abstract void validateStructure(Row row) throws ValidationException;

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
