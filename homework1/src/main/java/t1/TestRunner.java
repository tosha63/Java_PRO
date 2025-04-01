package t1;

import t1.annotation.AfterSuite;
import t1.annotation.AfterTest;
import t1.annotation.BeforeSuite;
import t1.annotation.BeforeTest;
import t1.annotation.CsvSource;
import t1.annotation.Test;
import t1.example.AnnotationTest;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

public class TestRunner {

    private static final Integer MAX_COUNT_METHOD = 1;
    private static final Integer MIN_PRIORITY = 1;
    private static final Integer MAX_PRIORITY = 10;

    public static void main(String[] args) {
        runTests(AnnotationTest.class);
    }

    public static void runTests(Class<?> clazz) {
        try {
            Object object = clazz.getConstructor().newInstance();
            final var beforeSuiteMethod = getSuiteMethod(clazz, BeforeSuite.class);
            final var afterSuiteMethod = getSuiteMethod(clazz, AfterSuite.class);
            invokeStaticMethod(beforeSuiteMethod, BeforeSuite.class, object);
            invokeMethodWithTestAnnotation(clazz, object);
            invokeStaticMethod(afterSuiteMethod, AfterSuite.class, object);
            invokeMethodWithCsvSourceAnnotation(clazz, object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    private static Method getSuiteMethod(Class<?> clazz, Class<? extends Annotation> annotationClass) {
        final var methods = Arrays.stream(clazz.getDeclaredMethods())
                                  .filter(method -> method.isAnnotationPresent(annotationClass))
                                  .filter(method -> Modifier.isStatic(method.getModifiers()))
                                  .toList();
        if (methods.size() > MAX_COUNT_METHOD) {
            throw new IllegalStateException(String.format("Количество статических методов с аннотацией %s не равно одному%n", annotationClass.getSimpleName()));
        } else {
            if (!methods.isEmpty()) {
                final var method = methods.get(0);
                System.out.printf("Метод помеченный аннотацией %s с именем %s количество:%s%n", annotationClass.getSimpleName(), method.getName(), methods.size());
                return method;
            } else {
                return null;
            }
        }
    }

    private static void invokeStaticMethod(Method method, Class<? extends Annotation> annotationClass, Object object) {
        if (Objects.nonNull(method)) {
            System.out.printf("Выполняется метод с аннотацией %s с именем: %s%n",
                              method.getAnnotation(annotationClass)
                                    .annotationType()
                                    .getSimpleName(),
                              method.getName());
            try {
                method.invoke(object);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void invokeMethodWithTestAnnotation(Class<?> clazz, Object object) {
        final var beforeTestMethod = getBeforeAndAfterTestMethod(clazz, BeforeTest.class);
        final var afterTestMethod = getBeforeAndAfterTestMethod(clazz, AfterTest.class);
        Arrays.stream(clazz.getDeclaredMethods())
              .filter(method -> method.isAnnotationPresent(Test.class))
              .sorted(Comparator.comparingInt(method -> method.getAnnotation(Test.class).priority()))
              .forEach(method -> {
                  final var testAnnotation = method.getAnnotation(Test.class);
                  final var testAnnotationName = testAnnotation.annotationType()
                                                               .getSimpleName();
                  final var priority = testAnnotation.priority();
                  if (priority < MIN_PRIORITY || priority > MAX_PRIORITY) {
                      throw new RuntimeException("Значение priority не входит в диапазон от 1 до 10");
                  }
                  System.out.printf("Выполняется метод с аннотацией %s с именем: %s и приоритетом: %s%n", testAnnotationName, method.getName(), priority);
                  try {
                      if (Objects.nonNull(beforeTestMethod)) {
                          beforeTestMethod.invoke(object);
                      }
                      method.invoke(object);
                      if (Objects.nonNull(afterTestMethod)) {
                          afterTestMethod.invoke(object);
                      }
                  } catch (Exception e) {
                      throw new RuntimeException(e);
                  }
              });
    }

    private static Method getBeforeAndAfterTestMethod(Class<?> clazz, Class<? extends Annotation> annotationClass) {
        return Arrays.stream(clazz.getDeclaredMethods())
                     .filter(method -> method.isAnnotationPresent(annotationClass))
                     .findFirst()
                     .orElse(null);
    }

    public static void invokeMethodWithCsvSourceAnnotation(Class<?> clazz, Object object) {
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
                      method.invoke(object, parameterCasts);
                  } catch (Exception e) {
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
