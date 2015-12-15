package restaurant.message;

import restaurant.Order;

/**
 * User: mickdudley
 * Date: 15/12/2015
 */
public class OrderMessage extends Message {
    private final Order order;

    public OrderMessage(Order order) {
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }
}
