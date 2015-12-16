package restaurant.message;

import restaurant.Order;

public class PayOrderMessage extends OrderMessage {
    public PayOrderMessage(Order order, Message cause) {
        super(order, cause);
    }
}
