package Trader;

public class NaiveTrader {

    public static void main(String[] args) {

        Runnable task = () ->
                System.out.println(
                        Thread.currentThread().getName());

        Thread thread = new Thread(task);

        // run() executes on current thread
        // start() creates a new thread

        thread.start();
    }
}
