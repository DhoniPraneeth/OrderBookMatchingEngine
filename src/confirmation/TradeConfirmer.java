package confirmation;

import model.Trade;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;

public class TradeConfirmer {

    private ExecutorService confirmationExecutor;

    private List<Trade> tradeHistory;

    private Random random;

    public TradeConfirmer() {
    }
}
