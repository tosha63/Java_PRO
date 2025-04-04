package t1.stream;

import t1.stream.dto.Employee;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class StreamApiTest {
    public static void main(String[] args) {
        /**1 Найдите в списке целых чисел 3-е наибольшее число*/
        List<Integer> integers = Arrays.asList(5, 2, 10, 9, 4, 3, 10, 1, 13);
        Integer max = integers.stream()
                            .sorted(Comparator.reverseOrder())
                            .skip(2)
                            .limit(1)
                            .findFirst()
                            .orElseThrow();

        System.out.println(max);
        System.out.println("-------------------------");

        /**2 Найдите в списке целых чисел 3-е наибольшее «уникальное» число*/
        Integer maxUnique = integers.stream()
                            .distinct()
                            .sorted(Comparator.reverseOrder())
                            .skip(2)
                            .limit(1)
                            .findFirst()
                            .orElseThrow();

        System.out.println(maxUnique);

        System.out.println("-------------------------");

        /**3 Необходимо получить список имен 3 самых старших сотрудников
         * с должностью «Инженер», в порядке убывания возраста*/

        getEmployees().stream()
                      .filter(employee -> employee.getPosition() == Employee.Position.ENGINEER)
                      .sorted((o1, o2) -> o2.getAge() - o1.getAge())
                      .limit(3)
                      .map(Employee::getName)
                      .forEach(System.out::println);

        System.out.println("-------------------------");

        /**4 Посчитайте средний возраст сотрудников с должностью «Инженер»*/
        getEmployees().stream()
                      .filter(employee -> employee.getPosition() == Employee.Position.ENGINEER)
                      .mapToInt(Employee::getAge)
                      .average()
                      .ifPresent(System.out::println);
        //35 + 48 + 29 + 33 + 43 = 188 / 5 = 37,6
        System.out.println("-------------------------");

        /**5 Найдите в списке слов самое длинное*/
        getWords().stream()
                  .max(Comparator.comparing(String::length))
                  .ifPresent(System.out::println);

        System.out.println("-------------------------");

        /**6 Имеется строка с набором слов в нижнем регистре, разделенных пробелом.
         * Постройте хеш-мапы, в которой будут хранится пары:
         * слово - сколько раз оно встречается во входной строке*/

        Arrays.stream("cat dog bird cat cow dog bird cow pig cat bird bird cow cow cow".split(" "))
              .collect(Collectors.groupingBy(string -> string, Collectors.counting()))
              .forEach((string, aLong) -> System.out.println(string + " - " + aLong));

        System.out.println("-------------------------");

        /**7 Отпечатайте в консоль строки из списка в порядке увеличения длины слова,
         * если слова имеют одинаковую длины, то должен быть сохранен алфавитный порядок*/
        getWords().stream()
                  .sorted(Comparator.comparing(String::length)
                                    .thenComparing(Comparator.naturalOrder()))
                  .forEach(System.out::println);

        System.out.println("-------------------------");

        /**8 Имеется массив строк, в каждой из которых лежит набор из 5 слов, разделенных пробелом,
         * найдите среди всех слов самое длинное, если таких слов несколько, получите любое из них*/
        Arrays.stream(new String[]{
                      "cat cot doggy dacha sunshine",
                      "moon stars sky sea tree",
                      "aberration abbreviate abduction absently abaft",
                      "apple banana cherry date elderberry",
                      "frog grape honey ice jelly",
                      "kiwi lemon mango orange peach"})
              .map(string -> string.split(" "))
              .flatMap(Arrays::stream)
              .max(Comparator.comparing(String::length))
              .ifPresent(System.out::println);
    }


    private static List<Employee> getEmployees() {
        return new ArrayList<>(Arrays.asList(new Employee("Bob", 55, Employee.Position.DIRECTOR),
                                             new Employee("Klara", 36, Employee.Position.MANAGER),
                                             new Employee("Hank", 35, Employee.Position.ENGINEER),
                                             new Employee("Anton", 48, Employee.Position.ENGINEER),
                                             new Employee("Mikhail", 29, Employee.Position.ENGINEER),
                                             new Employee("Ivan", 31, Employee.Position.TECHNICIAN),
                                             new Employee("Mari", 25, Employee.Position.MANAGER),
                                             new Employee("Aleksandr", 59, Employee.Position.TECHNICIAN),
                                             new Employee("Oleg", 33, Employee.Position.ENGINEER),
                                             new Employee("Polina", 43, Employee.Position.ENGINEER)));
    }

    private static List<String> getWords() {
        return new ArrayList<>(Arrays.asList("cat",
                                             "cot",
                                             "doggy",
                                             "dacha",
                                             "sunshine",
                                             "moon",
                                             "stars",
                                             "sky",
                                             "sea",
                                             "tree",
                                             "flower",
                                             "bird"));
    }
}
