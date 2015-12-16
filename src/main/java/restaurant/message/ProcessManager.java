package restaurant.message;

/**
 * Created by jan on 16/12/15.
 */
public class ProcessManager implements MessageHandler {

    private MessageBus bus;

    public ProcessManager(MessageBus bus) {
        this.bus = bus;
    }

    @Override
    public void handle(OrderMessage message) {
        System.out.println("ProcessManager, message: " + message.getClass().getSimpleName());
        if (message instanceof OrderPlacedMessage) {
            bus.publish(new CookOrderMessage(message.getOrder(), message));
        } else if (message instanceof OrderCookedMessage) {
            bus.publish(new PriceOrderMessage(message.getOrder(), message));
        } else if (message instanceof OrderPricedMessage) {
            bus.publish(new PayOrderMessage(message.getOrder(), message));
        }
    }
}
