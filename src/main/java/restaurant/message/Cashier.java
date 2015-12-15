package restaurant.message;

import restaurant.HandleOrder;
import restaurant.Order;
import restaurant.TopicBasedPubSub;

import java.util.ArrayList;
import java.util.List;

public class Cashier implements MessageHandler{
    private MessageBasedPubSub bus;

    public Cashier(MessageBasedPubSub bus) {
        this.bus = bus;
    }

    private List<Order> paidOrders = new ArrayList<>();

    public List<Order> getPaidOrders() {
        return paidOrders;
    }

    @Override
    public void handle(OrderMessage message) {
        System.out.println(getClass().getSimpleName() + " handle");
        message.getOrder().setPaid(true);
        message.getOrder().setPaymentMethod("card");
        paidOrders.add(message.getOrder());

        bus.publish(new OrderPaidMessage(message.getOrder()));

    }
}
