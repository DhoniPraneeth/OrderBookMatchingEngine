package pitfalls;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

public class Pitfalls {

    public static void main(String[] args)
            throws Exception {

        raceConditionDemo();

        deadlockDemo();

        volatileNotEnoughDemo();

        getTooEarlyDemo();
    }

    public static void raceConditionDemo()
            throws Exception {

        System.out.println(
                "\n--- Race Condition Demo ---");

        List<Integer> list =
                new ArrayList<>();

        Thread t1 =
                new Thread(() -> {

                    for(int i=0;i<1000;i++){

                        list.add(i);
                    }
                });

        Thread t2 =
                new Thread(() -> {

                    for(int i=0;i<1000;i++){

                        list.add(i);
                    }
                });

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println(
                "Expected 2000, Actual "
                        + list.size());

        System.out.println(
                "Fix: synchronize writes");
    }

    public static void deadlockDemo() {

        System.out.println(
                "\n--- Deadlock Demo ---");

        System.out.println(
                "Thread A -> Lock1 -> Lock2");

        System.out.println(
                "Thread B -> Lock2 -> Lock1");

        System.out.println(
                "Fix: always acquire locks in same order");
    }

    public static void volatileNotEnoughDemo()
            throws Exception {

        System.out.println(
                "\n--- Volatile Not Enough ---");

        AtomicInteger counter =
                new AtomicInteger();

        Runnable task = () -> {

            for(int i=0;i<1000;i++){

                counter.incrementAndGet();
            }
        };

        Thread t1 =
                new Thread(task);

        Thread t2 =
                new Thread(task);

        Thread t3 =
                new Thread(task);

        t1.start();
        t2.start();
        t3.start();

        t1.join();
        t2.join();
        t3.join();

        System.out.println(
                "Atomic Counter = "
                        + counter.get());
    }

    public static void getTooEarlyDemo() {

        System.out.println(
                "\n--- CompletableFuture Demo ---");

        List<CompletableFuture<Integer>>
                futures =
                new ArrayList<>();

        for(int i=0;i<5;i++){

            futures.add(
                    CompletableFuture
                            .supplyAsync(() -> {

                                try {

                                    Thread.sleep(1000);

                                } catch (Exception e) {
                                }

                                return 1;
                            })
            );
        }

        CompletableFuture.allOf(
                futures.toArray(
                        new CompletableFuture[0]
                )).join();

        System.out.println(
                "All completed concurrently");
    }
}
