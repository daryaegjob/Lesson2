package taskmanager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Простой самописный CSV-парсер (без сторонних библиотек — как и просили).
 * Формат: первая строка — заголовок "id,title,description,status" (порядок
 * колонок может быть любым, парсер определяет его по заголовку), дальше —
 * строки данных.
 *
 * Ограничение (учебное упрощение): не поддерживает запятые внутри значений
 * и экранирование кавычками. Для реального проекта тут стоило бы взять
 * готовую библиотеку (например, Apache Commons CSV).
 */
public class CsvTaskParser implements TaskFileParser {

    private static final String DELIMITER = ",";

    @Override
    public List<Task> parse(String filePath) throws FileParsingException {
        List<Task> tasks = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String headerLine = reader.readLine();
            if (headerLine == null) {
                throw new FileParsingException("CSV-файл " + filePath + " пуст");
            }
            String[] header = headerLine.split(DELIMITER);
            int idIndex = indexOf(header, "id");
            int titleIndex = indexOf(header, "title");
            int descriptionIndex = indexOf(header, "description");
            int statusIndex = indexOf(header, "status");

            String line;
            int lineNumber = 1;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.isBlank()) {
                    continue;
                }

                String[] fields = line.split(DELIMITER, -1);
                if (fields.length != header.length) {
                    throw new FileParsingException(
                            "Строка " + lineNumber + " в " + filePath
                                    + " не соответствует заголовку (колонок: " + fields.length
                                    + ", ожидалось: " + header.length + ")");
                }

                try {
                    int id = Integer.parseInt(fields[idIndex].trim());
                    tasks.add(new Task(
                            id,
                            fields[titleIndex].trim(),
                            fields[descriptionIndex].trim(),
                            fields[statusIndex].trim()));
                } catch (NumberFormatException e) {
                    throw new FileParsingException("Строка " + lineNumber + ": поле id должно быть числом");
                }
            }
        } catch (IOException e) {
            throw new FileParsingException("Не удалось прочитать CSV-файл " + filePath + ": " + e.getMessage());
        }

        return tasks;
    }

    private int indexOf(String[] header, String columnName) throws FileParsingException {
        for (int i = 0; i < header.length; i++) {
            if (header[i].trim().equalsIgnoreCase(columnName)) {
                return i;
            }
        }
        throw new FileParsingException("В заголовке CSV не найдена колонка \"" + columnName + "\"");
    }
}
