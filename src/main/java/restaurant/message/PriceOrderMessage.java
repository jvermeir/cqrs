package restaurant.message;

import restaurant.Order;

public class PriceOrderMessage extends OrderMessage {
    public PriceOrderMessage(Order order) {
        super(order);
    }
}
