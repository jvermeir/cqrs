package restaurant.message;

import restaurant.Order;

/**
 * User: mickdudley
 * Date: 15/12/2015
 */
public class OrderPaidMessage extends OrderMessage {
    public OrderPaidMessage(Order order) {
        super(order);
    }
}
