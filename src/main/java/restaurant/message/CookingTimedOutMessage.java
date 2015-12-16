package restaurant.message;

import restaurant.Order;

/**
 * User: mickdudley
 * Date: 15/12/2015
 */
public class CookingTimedOutMessage extends OrderMessage {
    private final int count;

    public CookingTimedOutMessage(Order order, Message cause, int count) {
        super(order, cause);
        this.count = count;
    }

    public int getCount() {
        return count;
    }
}
