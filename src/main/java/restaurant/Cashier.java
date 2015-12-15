package restaurant;

import java.util.ArrayList;
import java.util.List;

public class Cashier implements HandleOrder{
    private HandleOrder handler;
    private TopicBasedPubSub bus;

    public Cashier(TopicBasedPubSub bus) {
        this.bus = bus;
    }
    public Cashier(HandleOrder handler) {
        this.handler = handler;
    }

    private List<Order> paidOrders = new ArrayList<>();

    public List<Order> getPaidOrders() {
        return paidOrders;
    }

    @Override
    public void handle(Order order) {
        System.out.println(getClass().getSimpleName() + " handle");
        order.setPaid(true);
        order.setPaymentMethod("card");
        paidOrders.add(order);
        if (bus == null) {
            handler.handle(order);
        }
        else {
            bus.publish("orderPaid", order);
        }
    }
}
