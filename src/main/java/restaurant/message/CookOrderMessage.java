package restaurant.message;

import restaurant.Order;

public class CookOrderMessage extends OrderMessage {
    public CookOrderMessage(Order order) {
        super(order);
    }
}
