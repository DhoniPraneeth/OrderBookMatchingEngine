package pitfalls;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

public class Pitfalls {
    private static volatile int count = 0;
    public static void main(String[] args)
            throws Exception {

        raceConditionDemo();

        volatileNotEnoughDemo();

        getTooEarlyDemo();

        deadlockDemo();

    }

    public static void raceConditionDemo() throws Exception {

        System.out.println("\n--- Race Condition Demo ---");
        List<Integer> list=new ArrayList<>();
        Pitfalls lock=new Pitfalls();
        //List<Integer> list = Collections.synchronizedList(new ArrayList<>());

        Runnable task = () -> {
//            synchronized (lock){
//                try {
//                    for(int i = 0; i < 1000; i++) {
//                        list.add(i);
//                    }
//                } catch(Exception e) {
//
//                    System.out.println(
//                            "Race Condition Observed: "
//                                    + e.getClass().getSimpleName());
//                }
//            }
            try {
                for(int i = 0; i < 1000; i++) {
                    list.add(i);
                }
            } catch(Exception e) {

                System.out.println(
                        "Race Condition Observed: "
                                + e.getClass().getSimpleName());
            }
        };

        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println(
                "Expected Size = 2000");

        System.out.println(
                "Actual Size = " + list.size());

        System.out.println(
                "Fix: synchronize writes or use Collections.synchronizedList()");
    }
    public static void deadlockDemo() throws InterruptedException {

        System.out.println("\n--- Deadlock Demo ---");

        Object lock1 = new Object();
        Object lock2 = new Object();

        Thread threadA = new Thread(() -> {

            synchronized (lock1) {

                System.out.println(
                        "Thread A acquired Lock1");

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                System.out.println(
                        "Thread A waiting for Lock2");

                synchronized (lock2) {

                    System.out.println(
                            "Thread A acquired Lock2");
                }
            }
        });

        Thread threadB = new Thread(() -> {

            synchronized (lock2) {

                System.out.println(
                        "Thread B acquired Lock2");

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                System.out.println(
                        "Thread B waiting for Lock1");

                synchronized (lock1) {

                    System.out.println(
                            "Thread B acquired Lock1");
                }
            }
        });

        threadA.start();
        threadB.start();
        threadA.join();
        threadB.join();
    }

    public static void volatileNotEnoughDemo()
            throws Exception {

        System.out.println(
                "\n--- Volatile Not Enough ---");

        AtomicInteger counter =
                new AtomicInteger();


        Runnable task = () -> {

            for(int i=0;i<1000;i++){
                count++;
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
        System.out.println(
                "Volatile Counter = "
                        + count);
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
