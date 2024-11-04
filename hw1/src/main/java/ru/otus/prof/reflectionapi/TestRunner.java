package ru.otus.prof.reflectionapi;

import ru.otus.prof.reflectionapi.annotations.AfterSuite;
import ru.otus.prof.reflectionapi.annotations.BeforeSuite;
import ru.otus.prof.reflectionapi.annotations.Disabled;
import ru.otus.prof.reflectionapi.annotations.Test;
import ru.otus.prof.reflectionapi.exeptions.AnnotationException;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class TestRunner {
    public static void run(Class<?> testSuiteClass) {
        int success = 0;
        int fail = 0;

        Method beforeSuiteMethod = null;
        Method afterSuiteMethod = null;
        List<Method> testMethods = new ArrayList<>();

        //сканируем все методы, собираем, проверяем
        for (Method method : testSuiteClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Disabled.class)) {
                Disabled disabledAnnotation = method.getAnnotation(Disabled.class);
                System.out.println("Method: " + method.getName() + " is disabled: " + disabledAnnotation.reason());
            }
            if (method.isAnnotationPresent(BeforeSuite.class)) {
                if (beforeSuiteMethod != null) {
                    throw new AnnotationException("Only one @BeforeSuite method");
                }
                beforeSuiteMethod = method;
            }
            if (method.isAnnotationPresent(AfterSuite.class)) {
                if (afterSuiteMethod != null) {
                    throw new AnnotationException("Only one @AfterSuite method");
                }
                afterSuiteMethod = method;
            }
            if (method.isAnnotationPresent(Test.class)) {
                //проверяем правильность проставленных аннотаций
                checkAnnotations(method);
                //Добавляем в пулл
                testMethods.add(method);
            }
        }


        //сортируем по приоритету
        testMethods.sort(Comparator.comparingInt((Method m) -> m.getAnnotation(Test.class).value()).reversed());

        //запускаем
        try {
            if (beforeSuiteMethod != null) {
                beforeSuiteMethod.invoke(null);
            }
            for (Method method : testMethods) {
                try {
                    method.invoke(null);
                    success++;
                } catch (Exception e) {
                    System.out.println("Test " + method.getName() + " failed " + e.getCause());
                    fail++;
                }
            }
            if (afterSuiteMethod != null) {
                afterSuiteMethod.invoke(null);
            }

        } catch (Exception e) {
            throw new RuntimeException("Error while running test ", e);
        }

        System.out.println("Test Results:");
        System.out.println("Total: " + (success + fail));
        System.out.println("Passed: " + success);
        System.out.println("Failed: " + fail);

    }


    private static void checkAnnotations(Method method) {
        if (method.isAnnotationPresent(BeforeSuite.class) || method.isAnnotationPresent(AfterSuite.class)) {
            throw new AnnotationException("@Test method cannot be annotated with @BeforeSuite/@AfterSuite");
        }
        int priority = method.getAnnotation(Test.class).value();
        if (priority < 1 || priority > 10) {
            throw new AnnotationException("Priority must be between 1 and 10");
        }
    }

}
