package restaurant.message;

/**
 * Created by jan on 16/12/15.
 */
public class PrePayProcessManager implements MessageHandler, ProcessManager {

    private MessageBus bus;

    public PrePayProcessManager(MessageBus bus) {
        this.bus = bus;
    }

    @Override
    public void handle(OrderMessage message) {
        System.out.println("PrePayProcessManager, message: " + message.getClass().getSimpleName());
        if (message instanceof OrderPlacedMessage) {
            bus.publish(new PriceOrderMessage(message.getOrder(), message));
        } else if (message instanceof OrderPricedMessage) {
            bus.publish(new PayOrderMessage(message.getOrder(), message));
        } else if (message instanceof OrderPaidMessage) {
            bus.publish(new CookOrderMessage(message.getOrder(), message));
        } else if (message instanceof OrderCookedMessage) {
            bus.publish(new OrderCompleteMessage(message.getOrder(), message));
        }
    }
}
