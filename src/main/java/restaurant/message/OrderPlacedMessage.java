package restaurant.message;

import restaurant.Order;

/**
 * User: mickdudley
 * Date: 15/12/2015
 */
public class OrderPlacedMessage extends OrderMessage {
    private final long timestamp;
    public OrderPlacedMessage(Order order) {
        super(order);
        this.timestamp = System.currentTimeMillis();
    }
}
