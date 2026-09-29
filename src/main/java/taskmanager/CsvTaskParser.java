package taskmanager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Самописный CSV-парсер (без сторонних библиотек), на Stream API.
 *
 * Формат: первая строка — заголовок "id,title,description,status" (порядок
 * колонок может быть любым), дальше — строки данных.
 *
 * Ограничение (учебное упрощение): не поддерживает запятые внутри значений
 * и экранирование кавычками.
 */
public class CsvTaskParser implements TaskFileParser {

    private static final String DELIMITER = ",";

    /** Строка данных вместе с её номером в файле — нужно для сообщений об ошибках. */
    private record IndexedLine(int lineNumber, String content) {
    }

    @Override
    public List<Task> parse(String filePath) throws FileParsingException {
        String[] header;
        List<String> dataLines;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String headerLine = reader.readLine();
            if (headerLine == null) {
                throw new FileParsingException("CSV-файл " + filePath + " пуст");
            }
            header = headerLine.split(DELIMITER);
            dataLines = reader.lines().collect(Collectors.toList());
        } catch (IOException e) {
            throw new FileParsingException("Не удалось прочитать CSV-файл " + filePath + ": " + e.getMessage());
        }

        int idIndex = indexOf(header, "id");
        int titleIndex = indexOf(header, "title");
        int descriptionIndex = indexOf(header, "description");
        int statusIndex = indexOf(header, "status");
        int expectedColumns = header.length;

        try {
            // +2 в номере строки: 1 — заголовок, дальше нумерация строк файла с единицы
            return IntStream.range(0, dataLines.size())
                    .mapToObj(i -> new IndexedLine(i + 2, dataLines.get(i)))
                    .filter(indexedLine -> !indexedLine.content().isBlank())
                    .map(indexedLine -> parseLine(
                            indexedLine.content(), indexedLine.lineNumber(), filePath,
                            expectedColumns, idIndex, titleIndex, descriptionIndex, statusIndex))
                    .collect(Collectors.toList());
        } catch (UncheckedFileParsingException e) {
            throw e.getCause();
        }
    }

    private Task parseLine(String line, int lineNumber, String filePath, int expectedColumns,
                            int idIndex, int titleIndex, int descriptionIndex, int statusIndex) {
        String[] fields = line.split(DELIMITER, -1);
        if (fields.length != expectedColumns) {
            throw new UncheckedFileParsingException(new FileParsingException(
                    "Строка " + lineNumber + " в " + filePath + " не соответствует заголовку (колонок: "
                            + fields.length + ", ожидалось: " + expectedColumns + ")"));
        }
        try {
            int id = Integer.parseInt(fields[idIndex].trim());
            return new Task(id, fields[titleIndex].trim(), fields[descriptionIndex].trim(), fields[statusIndex].trim());
        } catch (NumberFormatException e) {
            throw new UncheckedFileParsingException(
                    new FileParsingException("Строка " + lineNumber + ": поле id должно быть числом"));
        }
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
