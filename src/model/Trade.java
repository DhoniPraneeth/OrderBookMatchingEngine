package model;

public class Trade {
    private String tradeId;
    private Order buyOrder;
    private Order sellOrder;
    private double matchedPrice;
    private int matchedQuantity;

    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }

    public double getMatchedPrice() {
        return matchedPrice;
    }

    public void setMatchedPrice(double matchedPrice) {
        this.matchedPrice = matchedPrice;
    }

    public int getMatchedQuantity() {
        return matchedQuantity;
    }

    public void setMatchedQuantity(int matchedQuantity) {
        this.matchedQuantity = matchedQuantity;
    }

    public boolean isConfirmationSuccess() {
        return confirmationSuccess;
    }

    public void setConfirmationSuccess(boolean confirmationSuccess) {
        this.confirmationSuccess = confirmationSuccess;
    }

    private boolean confirmationSuccess;
    @Override
    public String toString() {
        return "Trade{" +
                "buyOrder=" + buyOrder +
                ", sellOrder=" + sellOrder +
                '}';
    }

    public Order getBuyOrder() {
        return buyOrder;
    }

    public void setBuyOrder(Order buyOrder) {
        this.buyOrder = buyOrder;
    }

    public Order getSellOrder() {
        return sellOrder;
    }

    public void setSellOrder(Order sellOrder) {
        this.sellOrder = sellOrder;
    }
}
