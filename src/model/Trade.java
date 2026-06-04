package model;

public class Trade {

    private final Order buyOrder;
    private final Order sellOrder;
    private final double matchedPrice;

    public Trade(Order buyOrder,
                 Order sellOrder,
                 double matchedPrice) {

        this.buyOrder = buyOrder;
        this.sellOrder = sellOrder;
        this.matchedPrice = matchedPrice;
    }

    public Order getBuyOrder() {
        return buyOrder;
    }

    public Order getSellOrder() {
        return sellOrder;
    }

    public double getMatchedPrice() {
        return matchedPrice;
    }

    @Override
    public String toString() {
        return buyOrder.getTraderName()
                + " <-> "
                + sellOrder.getTraderName()
                + " @ "
                + matchedPrice;
    }
}