import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Database database = new TaskDatabase();
        TaskManager taskManager = new TaskManager(database);
        Scanner scanner = new Scanner(System.in);

        printHelp();

        while (true) {
            System.out.print("\n> ");
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) {
                continue;
            }

            String[] parts = line.split(" ", 2);
            String command = parts[0].toLowerCase();
            String argument = parts.length > 1 ? parts[1].trim() : "";

            try {
                switch (command) {
                    case "add" -> handleAdd(taskManager, argument);
                    case "remove" -> handleRemove(taskManager, argument);
                    case "find" -> handleFind(taskManager, argument);
                    case "update" -> handleUpdate(taskManager, argument);
                    case "list" -> taskManager.getListTasks();
                    case "help" -> printHelp();
                    case "exit" -> {
                        System.out.println("Пока!");
                        return;
                    }
                    default -> System.out.println("Неизвестная команда. Введите 'help' для списка команд.");
                }
            } catch (ValidationException e) {
                System.out.println("Ошибка валидации: " + e.getMessage());
            } catch (TaskNotFoundException e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }
    }

    private static void handleAdd(TaskManager manager, String argument) throws ValidationException {
        // формат: add Название|Описание|Статус
        String[] fields = argument.split("\\|", -1);
        if (fields.length < 3) {
            System.out.println("Формат: add Название|Описание|Статус");
            return;
        }
        Task task = manager.addTask(fields[0].trim(), fields[1].trim(), fields[2].trim());
        System.out.println("Добавлена задача: " + task);
    }

    private static void handleRemove(TaskManager manager, String argument) throws TaskNotFoundException {
        Integer id = parseId(argument);
        if (id == null) {
            return;
        }
        manager.removeTask(id);
        System.out.println("Задача #" + id + " удалена.");
    }

    private static void handleFind(TaskManager manager, String argument) {
        if (argument.isEmpty()) {
            System.out.println("Укажите название для поиска: find <название>");
            return;
        }
        Task[] found = manager.findTaskByTitle(argument);
        if (found.length == 0) {
            System.out.println("Ничего не найдено по запросу: " + argument);
            return;
        }
        for (Task task : found) {
            System.out.println(task);
        }
    }

    private static void handleUpdate(TaskManager manager, String argument)
            throws TaskNotFoundException, ValidationException {
        // формат: update id|Название|Описание|Статус  (пустое поле = оставить как есть)
        String[] fields = argument.split("\\|", -1);
        if (fields.length < 4) {
            System.out.println("Формат: update id|Название|Описание|Статус");
            return;
        }
        Integer id = parseId(fields[0]);
        if (id == null) {
            return;
        }

        String title = fields[1].trim().isEmpty() ? null : fields[1].trim();
        String description = fields[2].trim().isEmpty() ? null : fields[2].trim();
        String status = fields[3].trim().isEmpty() ? null : fields[3].trim();

        manager.updateTask(id, title, description, status);
        System.out.println("Задача #" + id + " обновлена.");
    }

    private static Integer parseId(String text) {
        try {
            return Integer.parseInt(text.trim());
        } catch (NumberFormatException e) {
            System.out.println("id должен быть числом.");
            return null;
        }
    }

    private static void printHelp() {
        System.out.println("Менеджер задач (ООП + БД на records). Команды:");
        System.out.println("  add <название>|<описание>|<статус>");
        System.out.println("  remove <id>");
        System.out.println("  find <название>");
        System.out.println("  update <id>|<название>|<описание>|<статус>   (пустое поле = не менять)");
        System.out.println("  list");
        System.out.println("  help");
        System.out.println("  exit");
    }
}
