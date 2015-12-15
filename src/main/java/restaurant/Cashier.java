package restaurant;

import java.util.ArrayList;
import java.util.List;

public class Cashier implements HandleOrder{
    private final HandleOrder handler;

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
        handler.handle(order);
        paidOrders.add(order);
    }
}
