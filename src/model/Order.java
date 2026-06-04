package model;

public class Order {
    private String orderId;
    private String traderName;
    private Side side;
    private Double price;
    private int quantity;

    public String getOrderId() {
        return orderId;
    }

    @Override
    public String toString() {
        return "Order: " +
                "orderId='" + orderId + '\'' +
                ", traderName='" + traderName + '\'' +
                ", side=" + side +
                ", price=" + price +
                ", quantity=" + quantity +
                '\n';
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getTraderName() {
        return traderName;
    }

    public void setTraderName(String traderName) {
        this.traderName = traderName;
    }

    public Side getSide() {
        return side;
    }

    public void setSide(Side side) {
        this.side = side;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
