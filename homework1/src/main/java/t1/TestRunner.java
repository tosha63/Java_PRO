package t1;

import t1.annotation.AfterSuite;
import t1.annotation.AfterTest;
import t1.annotation.BeforeSuite;
import t1.annotation.BeforeTest;
import t1.annotation.CsvSource;
import t1.annotation.Max;
import t1.annotation.Min;
import t1.annotation.Test;
import t1.example.AnnotationTest;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Stream;

public class TestRunner {

    private static final Integer MAX_COUNT_METHOD = 1;
    private static final Integer MIN_PRIORITY = 1;
    private static final Integer MAX_PRIORITY = 10;

    public static void main(String[] args) {
        runTests(AnnotationTest.class);
    }

    public static void runTests(Class<?> clazz) {
        checkMethodCount(clazz, BeforeSuite.class);
        checkMethodCount(clazz, AfterSuite.class);
        invokeStaticMethod(clazz, BeforeSuite.class);
        invokeMethodWithTestAnnotation(clazz);
        invokeStaticMethod(clazz, AfterSuite.class);
        invokeMethodWithCsvSourceAnnotation(clazz);
    }


    private static void checkMethodCount(Class<?> clazz, Class<? extends Annotation> annotationClass) {
        long countMethod = getStreamFilterStaticMethod(clazz, annotationClass)
                .count();
        if (countMethod > MAX_COUNT_METHOD) {
            throw new RuntimeException(String.format("Количество статических методов с аннотацией %s не равно одному%n", annotationClass.getSimpleName()));
        } else {
            getStreamFilterStaticMethod(clazz, annotationClass)
                    .findFirst()
                    .ifPresent(method -> System.out.printf("Метод помеченный аннотацией %s с именем %s количество:%s%n", annotationClass.getSimpleName(), method.getName(), countMethod));
        }
    }

    private static void invokeStaticMethod(Class<?> clazz, Class<? extends Annotation> annotationClass) {
        getStreamFilterStaticMethod(clazz, annotationClass)
                .findFirst()
                .ifPresent(method -> {
                    System.out.printf("Выполняется метод с аннотацией %s с именем: %s%n", method.getAnnotation(annotationClass)
                                                                                                .annotationType()
                                                                                                .getSimpleName(),
                                      method.getName());
                    try {
                        method.invoke(clazz.getConstructor().newInstance());
                    } catch (IllegalAccessException |
                             InvocationTargetException |
                             NoSuchMethodException |
                             InstantiationException e) {
                        throw new RuntimeException(e);
                    }
                });

    }

    private static Stream<Method> getStreamFilterStaticMethod(Class<?> clazz, Class<? extends Annotation> annotationClass) {
        return Arrays.stream(clazz.getDeclaredMethods())
                     .filter(method -> method.isAnnotationPresent(annotationClass))
                     .filter(method -> Modifier.isStatic(method.getModifiers()));
    }

    private static void invokeMethodWithTestAnnotation(Class<?> clazz) {
        Arrays.stream(clazz.getDeclaredMethods())
              .filter(method -> method.isAnnotationPresent(Test.class))
              .sorted(Comparator.comparingInt(method -> method.getAnnotation(Test.class).priority()))
              .forEach(method -> {
                  final var testAnnotation = method.getAnnotation(Test.class);
                  Method priority;
                  try {
                      priority = testAnnotation.annotationType().getDeclaredMethod("priority");
                  } catch (NoSuchMethodException e) {
                      throw new RuntimeException(e);
                  }
                  final var minAnnotation = priority.getAnnotation(Min.class);
                  final var maxAnnotation = priority.getAnnotation(Max.class);
                  if (minAnnotation.value() < MIN_PRIORITY || maxAnnotation.value() > MAX_PRIORITY) {
                      throw new RuntimeException("Диапазон значений priority не входит от 1 до 10");
                  }
                  System.out.printf("Выполняется метод с аннотацией %s с именем: %s и приоритетом: %s%n", testAnnotation.annotationType()
                                                                                                                        .getSimpleName(),
                                    method.getName(),
                                    testAnnotation.priority());
                  try {
                      invokeBeforeAndAfterTestMethod(clazz, BeforeTest.class);
                      method.invoke(clazz.getConstructor().newInstance());
                      invokeBeforeAndAfterTestMethod(clazz, AfterTest.class);
                  } catch (IllegalAccessException |
                           InvocationTargetException |
                           NoSuchMethodException |
                           InstantiationException e) {
                      throw new RuntimeException(e);
                  }
              });
    }

    private static void invokeBeforeAndAfterTestMethod(Class<?> clazz, Class<? extends Annotation> annotationClass) {
        Arrays.stream(clazz.getDeclaredMethods())
              .filter(method -> method.isAnnotationPresent(annotationClass))
              .forEach(method -> {
                  try {
                      method.invoke(clazz.getConstructor().newInstance());
                  } catch (IllegalAccessException |
                           InvocationTargetException |
                           NoSuchMethodException |
                           InstantiationException e) {
                      throw new RuntimeException(e);
                  }
              });
    }

    public static void invokeMethodWithCsvSourceAnnotation(Class<?> clazz) {
        Arrays.stream(clazz.getDeclaredMethods())
              .filter(method -> method.isAnnotationPresent(CsvSource.class))
              .forEach(method -> {
                  final var csvSourceAnnotation = method.getAnnotation(CsvSource.class);
                  final var parameterTypes = method.getParameterTypes();
                  final var parameters = csvSourceAnnotation.parameters().split(",");
                  var parameterCasts = new Object[parameterTypes.length];
                  for (int i = 0; i < parameters.length; i++) {
                      if (parameters.length != parameterTypes.length) {
                          throw new RuntimeException("Не совпадение количества переданных параметров и количества аргументов метода");
                      }
                      parameterCasts[i] = castParameter(parameters[i].trim(), parameterTypes[i]);
                  }

                  try {
                      method.invoke(clazz.getConstructor().newInstance(), parameterCasts);
                  } catch (IllegalAccessException |
                           InvocationTargetException |
                           NoSuchMethodException |
                           InstantiationException e) {
                      throw new RuntimeException(e);
                  }
              });
    }

    private static Object castParameter(String paramValue, Class<?> paramType) {
        if (paramType == int.class || paramType == Integer.class) {
            return Integer.parseInt(paramValue);
        } else if (paramType == String.class) {
            return paramValue;
        } else if (paramType == boolean.class || paramType == Boolean.class) {
            return Boolean.parseBoolean(paramValue);
        } else if (paramType == long.class || paramType == Long.class) {
            return Long.parseLong(paramValue);
        } else if (paramType == double.class || paramType == Double.class) {
            return Double.parseDouble(paramValue);
        } else if (paramType == float.class || paramType == Float.class) {
            return Float.parseFloat(paramValue);
        } else if (paramType == short.class || paramType == Short.class) {
            return Short.parseShort(paramValue);
        } else if (paramType == byte.class || paramType == Byte.class) {
            return Byte.parseByte(paramValue);
        } else {
            throw new IllegalArgumentException("Неподдерживаемый тип параметра: " + paramType);
        }
    }
}
