package ru.otus.prof.reflectionapi;

import ru.otus.prof.reflectionapi.annotations.AfterSuite;
import ru.otus.prof.reflectionapi.annotations.BeforeSuite;
import ru.otus.prof.reflectionapi.annotations.Disabled;
import ru.otus.prof.reflectionapi.annotations.Test;

public class TestSuite {
    @Test
    public static void method1() {
        System.out.println("method 1");
    }

    @Test(3)
    public static void method2() {
        System.out.println("method 2");
    }

    @Test(9)
    public static void method3() {
        System.out.println("method 3");
    }

    @AfterSuite
    public static void method4() {
        System.out.println("AfterSuite");
    }

    @BeforeSuite
    public static void method5() {
        System.out.println("BeforeSuite");
    }

    @Test(5)
    public static void method6() {
        throw new RuntimeException("crash");
//        System.out.println("method 6");
    }

    @Disabled(reason = "test reason")
    public static void method7() {
        System.out.println("method 7");
    }
}
