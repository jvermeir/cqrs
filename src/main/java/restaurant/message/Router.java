package restaurant.message;

/**
 * Created by jan on 16/12/15.
 */
public class Router implements MessageHandler {

    private MessageBasedPubSub bus;

    public Router(MessageBasedPubSub bus) {
        this.bus = bus;
        bus.subscribe(OrderPlacedMessage.class, this);
        bus.subscribe(OrderCookedMessage.class, this);
        bus.subscribe(OrderPricedMessage.class, this);
    }

    @Override
    public void handle(OrderMessage message) {
        System.out.println("Router, message: " + message.getClass().getSimpleName());
        if (message instanceof OrderPlacedMessage) {
            bus.publish(new CookOrderMessage(message.getOrder(), message));
        } else if (message instanceof OrderCookedMessage) {
            bus.publish(new PriceOrderMessage(message.getOrder(), message));
        } else if (message instanceof OrderPricedMessage) {
            bus.publish(new PayOrderMessage(message.getOrder(), message));
        }
    }

}
