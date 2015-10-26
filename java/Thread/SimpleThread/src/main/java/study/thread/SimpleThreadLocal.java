package study.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SimpleThreadLocal {

    private static final ExecutorService EXECUTOR = Executors.newCachedThreadPool();
    private static final int MAXTHREADS = 10;
    private static final int MAXLOOP = 10;
    private static ThreadLocal<Integer> t_int = new ThreadLocal<Integer>() {

        @Override
        protected Integer initialValue() {
            return 1;
        }
    };

    public static void test() {

        try {
            System.out.println("Starting %d threads...\n");

            for (int i = 0; i < MAXTHREADS; ++i) {
                EXECUTOR.execute(new Runnable() {

                    public void run() {

                        for (int i = 1; i < MAXLOOP; ++i) {
                            System.out.printf("%s: %d\n", Thread.currentThread().getName(), t_int.get());
                            t_int.set(t_int.get() + 1);
                        }
                    }
                });
            }

            System.out.println("Wait for terminaison...");
            boolean alldone = EXECUTOR.awaitTermination(1, TimeUnit.SECONDS);
            System.out.printf("Bye! (alldone=%b)\n", alldone);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}