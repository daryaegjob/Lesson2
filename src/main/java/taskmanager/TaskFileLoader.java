package taskmanager;

import java.util.List;

/**
 * Определяет формат файла по расширению и делегирует разбор
 * соответствующему парсеру (JSON -> Jackson, CSV -> свой парсер).
 */
public class TaskFileLoader {

    public static List<Task> load(String filePath) throws FileParsingException {
        TaskFileParser parser = selectParser(filePath);
        return parser.parse(filePath);
    }

    private static TaskFileParser selectParser(String filePath) throws FileParsingException {
        String lowerCasePath = filePath.toLowerCase();
        if (lowerCasePath.endsWith(".json")) {
            return new JsonTaskParser();
        }
        if (lowerCasePath.endsWith(".csv")) {
            return new CsvTaskParser();
        }
        throw new FileParsingException("Неизвестный формат файла: " + filePath + " (ожидается .json или .csv)");
    }
}
