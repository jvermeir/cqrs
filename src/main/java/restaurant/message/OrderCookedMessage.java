package restaurant.message;

import restaurant.Order;

/**
 * User: mickdudley
 * Date: 15/12/2015
 */
public class OrderCookedMessage extends OrderMessage {
    public OrderCookedMessage(Order order) {
        super(order);
    }
}
