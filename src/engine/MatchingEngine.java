package engine;


import confirmation.TradeConfirmer;
import model.*;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

public class MatchingEngine implements Runnable {

    private final BlockingQueue<Order> queue;

    private final List<Order> buyBuffer =
            new ArrayList<>();

    private final List<Order> sellBuffer =
            new ArrayList<>();

    private final ReentrantLock lock =
            new ReentrantLock();

    private final List<Trade> tradeHistory;

    private final TradeConfirmer confirmer;

    private final ExecutorService confirmationPool;

    private final List<CompletableFuture<Boolean>>
            confirmationFutures;

    public volatile boolean marketOpen = true;

    public MatchingEngine(
            BlockingQueue<Order> queue,
            List<Trade> tradeHistory,
            TradeConfirmer confirmer,
            ExecutorService confirmationPool,
            List<CompletableFuture<Boolean>>
                    confirmationFutures) {

        this.queue = queue;
        this.tradeHistory = tradeHistory;
        this.confirmer = confirmer;
        this.confirmationPool = confirmationPool;
        this.confirmationFutures = confirmationFutures;
    }

    @Override
    public void run() {

        while(marketOpen || !queue.isEmpty()) {

            try {

                Order order =
                        queue.poll(
                                500,
                                TimeUnit.MILLISECONDS);

                if(order == null)
                    continue;

                if(lock.tryLock(
                        50,
                        TimeUnit.MILLISECONDS)) {

                    try {

                        process(order);

                    } finally {
                        lock.unlock();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void process(Order order){

        if(order.getSide() == OrderSide.BUY){
            for(Order sell : sellBuffer){
                if(order.getPrice()
                        >= sell.getPrice()){

                    Trade trade =
                            new Trade(
                                    order,
                                    sell,
                                    sell.getPrice());

                    sellBuffer.remove(sell);

                    synchronized (tradeHistory){
                        tradeHistory.add(trade);
                    }

                    confirmationFutures.add(
                            confirmer.confirmTrade(
                                    trade,
                                    confirmationPool));

                    return;
                }
            }

            buyBuffer.add(order);
        }
        else {

            for(Order buy : buyBuffer){

                if(buy.getPrice()
                        >= order.getPrice()){

                    Trade trade =
                            new Trade(
                                    buy,
                                    order,
                                    order.getPrice());

                    buyBuffer.remove(buy);

                    synchronized (tradeHistory){
                        tradeHistory.add(trade);
                    }

                    confirmationFutures.add(
                            confirmer.confirmTrade(
                                    trade,
                                    confirmationPool));

                    return;
                }
            }

            sellBuffer.add(order);
        }
    }

    public int unmatchedCount(){

        return buyBuffer.size()
                + sellBuffer.size();
    }
}
