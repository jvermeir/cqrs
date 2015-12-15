package restaurant.message;

import restaurant.HandleOrder;
import restaurant.Order;
import restaurant.TopicBasedPubSub;

/**
 * User: mickdudley
 * Date: 14/12/2015
 */
public class AsstManager implements MessageHandler {
    private MessageBasedPubSub bus;

    public AsstManager(MessageBasedPubSub bus) {
        this.bus = bus;
    }

    @Override
    public void handle(OrderMessage message) {
        if (message instanceof OrderCookedMessage) {
            System.out.println(getClass().getSimpleName() + " handle");
            message.getOrder().addSubTotal(9.99);
            message.getOrder().addTotal(11.98);
            message.getOrder().addTax(1.99);
            bus.publish(new OrderPricedMessage(message.getOrder()));
        } else {
            throw new TypeException("Wrong type of order for AsstManager: " + message.getClass());
        }

    }
}
