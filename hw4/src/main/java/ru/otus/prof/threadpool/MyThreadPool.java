package ru.otus.prof.threadpool;

import java.util.LinkedList;

public class MyThreadPool {
    private boolean isShutdown = false;
    private final int threadCount;
    private final WorkThread[] threads;
    private final LinkedList<Runnable> taskQueue;

    public MyThreadPool(int threadCount) {
        this.threadCount = threadCount;
        this.threads = new WorkThread[threadCount];
        this.taskQueue = new LinkedList<>();

        for (int i = 0; i < threadCount; i++) {
            threads[i] = new WorkThread();
            threads[i].start();
        }
    }

    public synchronized void execute(Runnable r) {
        if (isShutdown) {
            throw new IllegalStateException("ThreadPoll is stopped!");
        }
        taskQueue.add(r);
        this.notify();
    }

    public synchronized void shutdown() {
        isShutdown = true;
        this.notifyAll();
    }

    public class WorkThread extends Thread{
        @Override
        public void run() {
            while (true){
                Runnable task;
                synchronized (MyThreadPool.this){
                    while(taskQueue.isEmpty() && !isShutdown){
                        try {
                            MyThreadPool.this.wait();
                        } catch (InterruptedException e){
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }
                    if (isShutdown & taskQueue.isEmpty()){
                        break;
                    }

                    task = taskQueue.poll();
                }

                try{
                    if(task != null){
                        task.run();
                    }
                } catch (RuntimeException e){
                    e.printStackTrace();
                }
            }
        }
    }
}
