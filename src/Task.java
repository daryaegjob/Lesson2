/**
 * Задача. id неизменяемый (задаётся один раз при создании),
 * остальные поля можно менять через сеттеры — инкапсуляция.
 *
 * Класс больше ничего не знает про Row/Table — перевод в строку таблицы
 * и обратно теперь целиком в TaskDatabase. Task — чистая доменная модель.
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

    @Override
    public String toString() {
        return "#" + id + " [" + status + "] " + title + " — " + description;
    }
}
