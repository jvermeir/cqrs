package restaurant.message;

import restaurant.Order;

/**
 * User: mickdudley
 * Date: 15/12/2015
 */
public class DelayedSendMessage extends OrderMessage {
    private final long eta;
    private final OrderMessage messageToBeSent;

    public DelayedSendMessage(Order order, OrderMessage messageToBeSent, long eta) {
        super(order, messageToBeSent);
        this.eta = eta;
        this.messageToBeSent = messageToBeSent;
    }

    public long getEta() {
        return eta;
    }

    public OrderMessage getMessageToBeSent() {
        return messageToBeSent;
    }
}
