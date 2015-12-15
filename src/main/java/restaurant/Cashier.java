package restaurant;

public class Cashier implements HandleOrder{
    private final HandleOrder handler;

    public Cashier(HandleOrder handler) {
        this.handler = handler;
    }

    @Override
    public void handle(Order order) {
        System.out.println(getClass().getSimpleName() + " handle");
        order.setPaid(true);
        order.setPaymentMethod("card");
        handler.handle(order);
    }
}
