import confirmation.TradeConfirmer;
import engine.MatchingEngine;
import model.Order;
import model.Trade;
import Trader.TraderTask;
import util.OrderParser;

import java.util.*;
import java.util.concurrent.*;

public class Main {

    public static void main(String[] args)
            throws Exception {

        List<Order> orders =
                OrderParser.parse(
                        "src/sample-orders.txt");

        Map<String, List<Order>>
                traderOrders =
                new HashMap<>();

        for (Order order : orders) {

            traderOrders
                    .computeIfAbsent(
                            order.getTraderName(),
                            k -> new ArrayList<>())
                    .add(order);
        }

        /*
         * Producer Consumer Pattern
         */
        BlockingQueue<Order> queue =
                new LinkedBlockingQueue<>();

        List<Trade> tradeHistory =
                new ArrayList<>();

        List<CompletableFuture<Boolean>>
                confirmations =
                Collections.synchronizedList(
                        new ArrayList<>());

        /*
         * Fixed pool because trader count is known.
         *
         * Cached pool may create unbounded threads.
         */
        ExecutorService traderPool =
                Executors.newFixedThreadPool(
                        traderOrders.size());

        ExecutorService confirmationPool =
                Executors.newFixedThreadPool(4);

        TradeConfirmer confirmer =
                new TradeConfirmer();

        MatchingEngine engine =
                new MatchingEngine(
                        queue,
                        tradeHistory,
                        confirmer,
                        confirmationPool,
                        confirmations);

        Thread engineThread =
                new Thread(
                        engine,
                        "Matching-Engine");

        engineThread.start();

        List<Future<Integer>>
                traderResults =
                new ArrayList<>();

        try {

            for (Map.Entry<String,
                    List<Order>> entry
                    : traderOrders.entrySet()) {

                Callable<Integer> task =
                        new TraderTask(
                                entry.getKey(),
                                entry.getValue(),
                                queue);

                traderResults.add(
                        traderPool.submit(task));
            }

            int totalOrders = 0;

            for (Future<Integer> future
                    : traderResults) {

                totalOrders += future.get();
            }

            engine.marketOpen = false;

            engineThread.join();

            /*
             * Do NOT call get() immediately
             * after submission.
             *
             * That would serialize execution.
             *
             * allOf().join() waits for all
             * confirmations concurrently.
             */
            CompletableFuture.allOf(
                    confirmations.toArray(
                            new CompletableFuture[0]
                    )).join();

            long successCount =
                    confirmations
                            .stream()
                            .filter(future->future.join())
                            .count();

            long failureCount =
                    confirmations.size()
                            - successCount;

            System.out.println(
                    "\n========== SUMMARY ==========");

            System.out.println(
                    "Orders Submitted : "
                            + totalOrders);

            System.out.println(
                    "Trades Matched : "
                            + tradeHistory.size());

            System.out.println(
                    "Confirmations Success : "
                            + successCount);

            System.out.println(
                    "Confirmations Failed : "
                            + failureCount);

            System.out.println(
                    "Unmatched Orders : "
                            + engine.unmatchedCount());

        } finally {

            traderPool.shutdown();

            confirmationPool.shutdown();

            traderPool.awaitTermination(
                    5,
                    TimeUnit.SECONDS);

            confirmationPool.awaitTermination(
                    5,
                    TimeUnit.SECONDS);
        }
    }
}