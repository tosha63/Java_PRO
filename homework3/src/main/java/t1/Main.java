package t1;

import t1.multithreading.CustomThreadPool;

public class Main {
    public static void main(String[] args) {
        CustomThreadPool pool = new CustomThreadPool(10);

        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            final int taskNumber = i;
            pool.execute(() -> {
                System.out.println("Задача " + taskNumber + " запущена в потоке " + Thread.currentThread().getName());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println("Задача " + taskNumber + " завершена");
            });
        }

        pool.shutdown();
        pool.awaitTermination();
        long endTime = System.currentTimeMillis();
        double durationSeconds = (double) (endTime - startTime) / 1000;
        System.out.printf("Все задачи завершены и пул остановлен за %s секунд", durationSeconds);
    }
}