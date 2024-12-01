package ru.otus.prof.threadpool;


public class Main {
    public static void main(String[] args) {
        MyThreadPool myThreadPool = new MyThreadPool(5);

        for (int i = 0; i < 30; i++) {
            int taskNo = i;
            myThreadPool.execute(() -> {
                String message =
                        Thread.currentThread().getName()
                                + ": Task " + taskNo;
                System.out.println(message + " added");
                try {
                    Thread.sleep(3000);
                    System.out.println(message + " done");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
            if (i == 25){
                myThreadPool.shutdown();
                System.out.println("shutdown");
            }
        }
    }
}
