package restaurant.message;

import restaurant.Order;

/**
 * User: mickdudley
 * Date: 15/12/2015
 */
public class OrderDroppedMessage extends OrderMessage {
    public OrderDroppedMessage(Order order, Message cause) {
        super(order, cause);
    }
}
