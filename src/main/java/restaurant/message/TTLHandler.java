package restaurant.message;

import restaurant.Order;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by jan on 15/12/15.
 */
public class TTLHandler implements MessageHandler {
    private long timeCreated;
    private MessageHandler handler;
    private MessageBus bus;

    public TTLHandler(MessageHandler handler, MessageBus bus) {
        timeCreated = System.currentTimeMillis();
        this.bus = bus;
        this.handler = handler;
    }

    private static Queue<Order> lateOrders = new ConcurrentLinkedQueue<>();

    public static Queue<Order> getLateOrders() {
        return lateOrders;
    }

    @Override
    public void handle(OrderMessage message) {
        if (System.currentTimeMillis() > message.getOrder().getTimestamp() + 2000) {
            System.out.println("Dropping order " + message.getOrder());
            lateOrders.add(message.getOrder());
            bus.publish(new OrderDroppedMessage(message.getOrder(), message));
        }
        else {
            handler.handle(message);
        }

    }
}
