/**
 * Задача. id неизменяемый (задаётся один раз при создании),
 * остальные поля можно менять через сеттеры — инкапсуляция.
 */
public class Task {

    private final int id;
    private String title;
    private String description;
    private String status;

    public Task(int id, String title, String description, String status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /** Превращает задачу в строку таблицы, в порядке колонок TaskDatabase.COLUMNS. */
    public Row toRow() {
        return new Row(new String[]{String.valueOf(id), title, description, status});
    }

    /** Собирает задачу обратно из строки таблицы. */
    public static Task fromRow(Row row) {
        String[] c = row.columns();
        return new Task(Integer.parseInt(c[0]), c[1], c[2], c[3]);
    }

    @Override
    public String toString() {
        return "#" + id + " [" + status + "] " + title + " — " + description;
    }
}
