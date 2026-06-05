package confirmation;

import model.Trade;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

public class TradeConfirmer {

    private final Random random = new Random();

    public CompletableFuture<Boolean> confirmTrade(
            Trade trade,
            ExecutorService executor){

        return CompletableFuture

                .supplyAsync(() -> {

                    try {
                        Thread.sleep(500);

                        if(random.nextInt(10) == 0){
                            throw new RuntimeException(
                                    "Confirmation failed");
                        }

                        return true;

                    } catch (Exception e){
                        throw new RuntimeException(e);
                    }

                }, executor)

                .exceptionally(ex -> {

                    System.out.println(
                            "FAILED: " + trade);

                    return false;
                })

                .thenApply(result -> {

                    if(result){
                        System.out.println(
                                "CONFIRMED: " + trade);
                    }

                    return result;
                });
    }
}