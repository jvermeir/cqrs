package restaurant.message;

import restaurant.Order;

/**
 * User: mickdudley
 * Date: 15/12/2015
 */
public class OrderCompleteMessage extends OrderMessage {
    public OrderCompleteMessage(Order order, Message cause) {
        super(order, cause);
    }
}
