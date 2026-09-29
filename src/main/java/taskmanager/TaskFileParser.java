package taskmanager;

import java.util.List;

/**
 * Общий контракт для парсеров файла с задачами: неважно, JSON это или CSV,
 * снаружи получаем List<Task> либо FileParsingException.
 */
public interface TaskFileParser {
    List<Task> parse(String filePath) throws FileParsingException;
}
