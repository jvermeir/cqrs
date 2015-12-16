package restaurant.message;

import restaurant.Order;

/**
 * User: mickdudley
 * Date: 15/12/2015
 */
public class OrderPlacedMessage extends OrderMessage {
    private final long timestamp;
    public OrderPlacedMessage(Order order, Message cause) {
        super(order, cause);
        this.timestamp = System.currentTimeMillis();
    }
}
