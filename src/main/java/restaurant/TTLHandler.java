package restaurant;

import example1.Publisher;

/**
 * Created by jan on 15/12/15.
 */
public class TTLHandler implements HandleOrder {
    private long timeCreated;
    private HandleOrder handler;

    public TTLHandler(HandleOrder handler) {
        timeCreated = System.currentTimeMillis();
        this.handler = handler;
    }

    @Override
    public void handle(Order order) {
        if (System.currentTimeMillis() > order.getTimestamp() + 2000) {
            System.out.println("Dropping order " + order);
        }
        else {
            handler.handle(order);
        }

    }
}
