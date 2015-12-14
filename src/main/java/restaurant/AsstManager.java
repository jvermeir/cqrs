package restaurant;

/**
 * User: mickdudley
 * Date: 14/12/2015
 */
public class AsstManager implements HandleOrder {
    private final HandleOrder handler;

    public AsstManager(HandleOrder handler) {
        this.handler = handler;
    }


    @Override
    public void handle(Order order) {
        System.out.println(getClass().getSimpleName() + " handle");
        order.addSubTotal(1.23);
        order.addTotal(1.99);
        order.addTax(0.15);
        handler.handle(order);

    }
}
