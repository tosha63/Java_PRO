package t1.example;

import t1.annotation.AfterSuite;
import t1.annotation.AfterTest;
import t1.annotation.BeforeSuite;
import t1.annotation.BeforeTest;
import t1.annotation.CsvSource;
import t1.annotation.Test;

public class AnnotationTest {

    @BeforeSuite
    public static void testBeforeSuiteAnnotation() {
        System.out.println("Before Suite Annotation");
    }

    @AfterSuite
    public static void testAfterSuiteAnnotation() {
        System.out.println("After Suite Annotation");
    }

    @Test(priority = 3)
    public void testAnnotationPriorityThree() {
        System.out.println("Test Annotation Priority Three");
    }

    @Test(priority = 7)
    public void testAnnotationPrioritySeven() {
        System.out.println("Test Annotation Priority Seven");
    }

    @Test
    public void testAnnotationPriorityDefault() {
        System.out.println("Test Annotation Priority Default");
    }

    @Test(priority = 1)
    public void testAnnotationPriorityOne() {
        System.out.println("Annotation Priority One");
    }

    @AfterTest
    public void testAfterTestAnnotation() {
        System.out.println("After Test Annotation");
    }

    @BeforeTest
    public void testBeforeTestAnnotation() {
        System.out.println("Before Test Annotation");
    }

    @CsvSource(parameters = "2, test, true,55")
    public void testCsvSourceAnnotation(int number1,
                                        String string,
                                        boolean bool,
                                        Long number2) {
        System.out.printf("Csv Source Annotation with parameters int:%s, string:%s, boolean:%s, long:%s\n", number1, string, bool, number2);
    }

    @CsvSource(parameters = "99,false,true,test, 55")
    public void testCsvSourceAnnotation(int number1,
                                        boolean bool1,
                                        boolean bool2,
                                        String string,
                                        Long number2) {
        System.out.printf("Csv Source Annotation with parameters int:%s, boolean1:%s, boolean2:%s, string:%s, long:%s\n", number1, bool1, bool2, string, number2);
    }
}
