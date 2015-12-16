package restaurant.message;

import restaurant.Order;

public class CookOrderMessage extends OrderMessage {
    public CookOrderMessage(Order order, Message cause) {
        super(order, cause);
    }
}
