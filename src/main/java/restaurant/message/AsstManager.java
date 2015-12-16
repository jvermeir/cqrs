package restaurant.message;

/**
 * User: mickdudley
 * Date: 14/12/2015
 */
public class AsstManager implements MessageHandler {
    private MessageBus bus;

    public AsstManager(MessageBus bus) {
        this.bus = bus;
    }

    @Override
    public void handle(OrderMessage message) {
        if (message instanceof PriceOrderMessage) {
            System.out.println(getClass().getSimpleName() + " handle");
            message.getOrder().addSubTotal(9.99);
            message.getOrder().addTotal(11.98);
            message.getOrder().addTax(1.99);
            bus.publish(new OrderPricedMessage(message.getOrder(), message));
        } else {
            System.out.println("Wrong type of order for AsstManager: " + message.getClass());
        }

    }
}
