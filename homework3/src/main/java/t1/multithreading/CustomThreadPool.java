package t1.multithreading;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;

public class CustomThreadPool {
    private final LinkedList<Runnable> tasks;
    private final Thread[] workers;
    private final AtomicBoolean isShutdown = new AtomicBoolean(false);

    public CustomThreadPool(int corePoolSize) {
        if (corePoolSize <= 0) {
            throw new IllegalArgumentException("Размер пула должен быть больше 0");
        }
        this.tasks = new LinkedList<>();
        this.workers = new Thread[corePoolSize];
        for (int i = 0; i < corePoolSize; i++) {
            workers[i] = new Thread(new Task());
            workers[i].start();
        }
    }

    public void execute(Runnable task) {
        if (task == null) {
            throw new NullPointerException();
        }
        if (isShutdown.get()) {
            throw new IllegalStateException("Пул потоков завершен");
        }
        synchronized (tasks) {
            tasks.addLast(task);
            tasks.notify();
        }
    }

    public void shutdown() {
        isShutdown.set(true);
        synchronized (tasks) {
            tasks.notifyAll();
        }
    }

    public void awaitTermination() {
        for (Thread worker : workers) {
            try {
                worker.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Поток был прерван", e);
            }
        }
    }

    private class Task implements Runnable {
        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted() || !tasks.isEmpty()) {
                Runnable task = null;
                synchronized (tasks) {
                    while (tasks.isEmpty() && !isShutdown.get()) {
                        try {
                            tasks.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }
                    if (!tasks.isEmpty()) {
                        task = tasks.removeFirst();
                    } else if (isShutdown.get()) {
                        return;
                    }
                }
                if (task != null) {
                    task.run();
                }
            }
        }
    }
}
