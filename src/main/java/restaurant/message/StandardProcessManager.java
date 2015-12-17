package restaurant.message;

/**
 * Created by jan on 16/12/15.
 */
public class StandardProcessManager implements MessageHandler, ProcessManager {
    private static final int MAX_RETRY = 3;
    public static final int TIMEOUT = 2000;
    private boolean foodCooked;

    private MessageBus bus;

    public StandardProcessManager(MessageBus bus) {
        this.bus = bus;
    }

    @Override
    public void handle(OrderMessage message) {
        System.out.println("StandardProcessManager, message: " + message.getClass().getSimpleName());
        if (message instanceof OrderPlacedMessage) {
            CookOrderMessage cookOrderMessage = new CookOrderMessage(message.getOrder(), message);
            bus.publish(cookOrderMessage);
            CookingTimedOutMessage timedOutMessage = new CookingTimedOutMessage(message.getOrder(), cookOrderMessage, 1);
            bus.publish(new DelayedSendMessage(message.getOrder(), timedOutMessage, System.currentTimeMillis() + TIMEOUT));
        } else if (message instanceof OrderCookedMessage) {
            foodCooked = true;
            bus.publish(new PriceOrderMessage(message.getOrder(), message));
        } else if (message instanceof OrderPricedMessage) {
            bus.publish(new PayOrderMessage(message.getOrder(), message));
        } else if (message instanceof OrderPaidMessage) {
            bus.publish(new OrderCompleteMessage(message.getOrder(), message));
        } else if (message instanceof CookingTimedOutMessage) {
            if (!foodCooked) {
                CookingTimedOutMessage msg = (CookingTimedOutMessage) message;
                if (msg.getCount() <= MAX_RETRY) {
                    CookOrderMessage cookOrderMessage = new CookOrderMessage(message.getOrder(), message);
                    bus.publish(cookOrderMessage);
                    bus.publish(new DelayedSendMessage(message.getOrder(),
                            new CookingTimedOutMessage(msg.getOrder(), cookOrderMessage, msg.getCount() + 1),
                            System.currentTimeMillis() + TIMEOUT));
                }
                else {
                    // Tried too many times
                    bus.publish(new OrderDroppedMessage(message.getOrder(), message));
                }
            }
        }
    }
}
