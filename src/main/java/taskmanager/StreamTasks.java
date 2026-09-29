import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Занятие 5: функциональные принципы, Stream API, лямбды.
 * Каждая задача решена одной цепочкой методов (без промежуточных циклов).
 */
public class StreamTasks {

    private record Employee(String name, int age, String position) {
    }

    /* static test values because need only one instance of each */
    /* arraylist for exercises 1-3 */
    private static final ArrayList<Integer> intList =
            Stream.of(5, 2, 10, 9, 4, 3, 10, 1, 13)
                    .collect(Collectors.toCollection(ArrayList::new));

    /* arraylist of Employee for exercises 4-5 */
    private static final ArrayList<Employee> employeeList = Stream.of(
            new Employee("Людмила", 32, "Бухгалтер"),
            new Employee("Пахом", 72, "Инженер"),
            new Employee("Афанасий", 54, "Инженер"),
            new Employee("Геннадий", 46, "Уборщик"),
            new Employee("Коловрат", 56, "Инженер"),
            new Employee("Иннокентий", 60, "Сварщик"),
            new Employee("Евлампий", 64, "Инженер")
    ).collect(Collectors.toCollection(ArrayList::new));

    /* arraylist for exercise 6 and 8, also for generation stringLine */
    private static final ArrayList<String> stringList =
            Stream.of("shortstring1", "shortstr2", "someverylongstring",
                    "notlongstring", "hhortstring1", "shortstring1", "shortstring1")
                    .collect(Collectors.toCollection(ArrayList::new));

    /* string for exercise 7 generated from stringList */
    private static final String stringLine = String.join(" ", stringList);

    /* array of string for exercise 9 */
    private static final String[] stringArray = new String[]{
            "Lorem ipsum dolor sit amet",
            "consectetur adipiscing elit Duis elementum",
            "nunc nisl vitae pulvinar sapien",
            "posuere nec Nam nibh lacus",
            "aliquam non tempor in commodo",
            "in lorem Curabitur ac tempor"
    };

    public static void main(String[] args) {
        System.out.println("1) Без дубликатов: " + removeDuplicates(intList));
        System.out.println("2) 3-е наибольшее: " + thirdLargest(intList));
        System.out.println("3) 3-е наибольшее уникальное: " + thirdLargestUnique(intList));

        System.out.println("4) Топ-3 старших инженеров: " + topThreeOldestEngineers(employeeList));
        System.out.println("5) Средний возраст инженеров: " + averageEngineerAge(employeeList));

        System.out.println("6) Самое длинное слово: " + longestWord(stringList));
        System.out.println("7) Частоты слов: " + wordCounts(stringLine));

        System.out.println("8) Строки по длине, затем по алфавиту:");
        printSortedByLengthThenAlphabetically(stringList);

        System.out.println("9) Самое длинное слово среди всех строк: " + longestWordInLines(stringArray));
    }

    /* Exercise 1 */
    /* Реализуйте удаление из листа всех дубликатов */
    static <T> List<T> removeDuplicates(List<T> list) {
        return list.stream()
                .distinct()
                .collect(Collectors.toList());
    }

    /* Exercise 2 */
    /* Найдите в списке целых чисел 3-е наибольшее число (пример: 5 2 10 9 4 3 10 1 13 => 10) */
    static Integer thirdLargest(List<Integer> numbers) {
        return numbers.stream()
                .sorted(Comparator.reverseOrder())
                .skip(2)
                .findFirst()
                .orElse(null);
    }

    /* Exercise 3 */
    /* Найдите в списке целых чисел 3-е наибольшее «уникальное» число (пример: 5 2 10 9 4 3 10 1 13 => 9,
       в отличие от прошлой задачи здесь разные 10 считает за одно число) */
    static Integer thirdLargestUnique(List<Integer> numbers) {
        return numbers.stream()
                .distinct()
                .sorted(Comparator.reverseOrder())
                .skip(2)
                .findFirst()
                .orElse(null);
    }

    /* Exercise 4 */
    /* Имеется список объектов типа Сотрудник (имя, возраст, должность),
       необходимо получить список имен 3 самых старших сотрудников с должностью «Инженер», в порядке убывания возраста */
    static List<String> topThreeOldestEngineers(List<Employee> employees) {
        return employees.stream()
                .filter(employee -> employee.position().equals("Инженер"))
                .sorted(Comparator.comparingInt(Employee::age).reversed())
                .limit(3)
                .map(Employee::name)
                .collect(Collectors.toList());
    }

    /* Exercise 5 */
    /* Имеется список объектов типа Сотрудник (имя, возраст, должность),
       посчитайте средний возраст сотрудников с должностью «Инженер» */
    static double averageEngineerAge(List<Employee> employees) {
        return employees.stream()
                .filter(employee -> employee.position().equals("Инженер"))
                .mapToInt(Employee::age)
                .average()
                .orElse(0);
    }

    /* Exercise 6 */
    /* Найдите в списке слов самое длинное */
    static String longestWord(List<String> words) {
        return words.stream()
                .max(Comparator.comparingInt(String::length))
                .orElse(null);
    }

    /* Exercise 7 */
    /* Имеется строка с набором слов в нижнем регистре, разделенных пробелом.
       Постройте хеш-мапы, в которой будут хранится пары: слово - сколько раз оно встречается во входной строке */
    static Map<String, Long> wordCounts(String text) {
        return Arrays.stream(text.split("\\s+"))
                .collect(Collectors.groupingBy(word -> word, Collectors.counting()));
    }

    /* Exercise 8 */
    /* Отпечатайте в консоль строки из списка в порядке увеличения длины слова,
       если слова имеют одинаковую длины, то должен быть сохранен алфавитный порядок */
    static void printSortedByLengthThenAlphabetically(List<String> words) {
        words.stream()
                .sorted(Comparator.comparingInt(String::length).thenComparing(Comparator.naturalOrder()))
                .forEach(System.out::println);
    }

    /* Exercise 9 */
    /* Имеется массив строк, в каждой из которых лежит набор из 5 строк, разделенных пробелом,
       найдите среди всех слов самое длинное, если таких слов несколько, получите любое из них */
    static String longestWordInLines(String[] lines) {
        return Arrays.stream(lines)
                .flatMap(line -> Arrays.stream(line.split("\\s+")))
                .max(Comparator.comparingInt(String::length))
                .orElse(null);
    }
}
