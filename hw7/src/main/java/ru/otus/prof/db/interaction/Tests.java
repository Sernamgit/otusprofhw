package ru.otus.prof.db.interaction;

public class Tests {
    public static void main(String[] args) {
        StringBuilder test = new StringBuilder();


        for (int i = 0; i < 4; i++) {
            test.append(i).append(i);
            System.out.println(test);
        }
    }
}
