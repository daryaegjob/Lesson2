package taskmanager;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Парсит JSON-файл вида [{"id":1,"title":"...", ...}, ...] в список Task.
 * Jackson сам разбирает массив объектов и вызывает конструктор Task,
 * помеченный @JsonCreator (см. Task.java).
 */
public class JsonTaskParser implements TaskFileParser {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public List<Task> parse(String filePath) throws FileParsingException {
        try {
            Task[] tasks = objectMapper.readValue(new File(filePath), Task[].class);
            return Arrays.stream(tasks).collect(Collectors.toList());
        } catch (IOException e) {
            throw new FileParsingException("Не удалось разобрать JSON-файл " + filePath + ": " + e.getMessage());
        }
    }
}
