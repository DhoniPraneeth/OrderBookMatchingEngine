package model;

public class Order {

    private final String traderName;
    private final OrderSide side;
    private final double price;
    private final int quantity;

    public Order(String traderName,
                 OrderSide side,
                 double price,
                 int quantity) {

        this.traderName = traderName;
        this.side = side;
        this.price = price;
        this.quantity = quantity;
    }

    public String getTraderName() {
        return traderName;
    }

    public OrderSide getSide() {
        return side;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return traderName +
                " " +
                side +
                " " +
                price +
                " " +
                quantity;
    }
}