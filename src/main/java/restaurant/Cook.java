package restaurant;

public class Cook implements HandleOrder{
    private final HandleOrder handler;
    // Database of recipes


    public Cook (HandleOrder handler) {
        this.handler = handler;

    }


    @Override
    public void handle(Order order) {
        System.out.println(getClass().getSimpleName() + " handle");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException("Help");
        }
        order.setCookTime (300);
        // Add ingredients
        handler.handle(order);
    }
}
