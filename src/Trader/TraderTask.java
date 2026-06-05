package Trader;

import model.Order;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;

public class TraderTask implements Callable<Integer> {

    private final String traderName;
    private final List<Order> orders;
    private final BlockingQueue<Order> queue;

    public TraderTask(String traderName,
                      List<Order> orders,
                      BlockingQueue<Order> queue) {

        this.traderName = traderName;
        this.orders = orders;
        this.queue = queue;
    }


    @Override
    public Integer call() throws Exception {

        int submittedOrders = 0;

        for (Order order : orders) {

            Thread.sleep(200);

            queue.put(order);

            submittedOrders++;

            System.out.println(
                    traderName +
                            " submitted " +
                            order);
        }

        return submittedOrders;
    }
}