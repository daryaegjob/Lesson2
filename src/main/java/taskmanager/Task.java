package taskmanager;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Задача. id неизменяемый, остальные поля можно менять через сеттеры.
 *
 * Конструктор помечен @JsonCreator/@JsonProperty, чтобы Jackson мог
 * создавать Task напрямую из JSON без отдельного DTO-класса — у id
 * нет сеттера, поэтому обычная автоматическая привязка полей не сработала бы.
 */
public class Task {

    private final int id;
    private String title;
    private String description;
    private String status;

    @JsonCreator
    public Task(
            @JsonProperty("id") int id,
            @JsonProperty("title") String title,
            @JsonProperty("description") String description,
            @JsonProperty("status") String status) {
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
