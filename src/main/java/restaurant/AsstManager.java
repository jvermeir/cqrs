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
        order.addSubTotal(9.99);
        order.addTotal(11.98);
        order.addTax(1.99);
        handler.handle(order);

    }
}
