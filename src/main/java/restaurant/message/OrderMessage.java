package restaurant.message;

import restaurant.Order;

/**
 * User: mickdudley
 * Date: 15/12/2015
 */
public class OrderMessage extends Message {
    private final Order order;

    public OrderMessage(Order order, Message cause) {
        super(cause);
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }

    @Override
    public String toString() {
        return "messageID=" + getMessageId() +
                "\ncorrelationId=" + getCorrelationId() +
                "\ncausationId=" + getCausationId();
    }
}
