package restaurant.message;

import restaurant.Order;

/**
 * User: mickdudley
 * Date: 15/12/2015
 */
public class OrderPricedMessage extends OrderMessage {
    public OrderPricedMessage(Order order) {
        super(order);
    }
}
