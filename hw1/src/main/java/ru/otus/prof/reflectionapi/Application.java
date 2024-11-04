package ru.otus.prof.reflectionapi;

public class Application {
    public static void main(String[] args) {
        try {
            TestRunner.run(TestSuite.class);
        } catch (Exception e){
            System.out.println(e);
        }
    }
}
