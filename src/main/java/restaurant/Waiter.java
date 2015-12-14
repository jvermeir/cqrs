package restaurant;

import java.util.HashMap;
import java.util.Map;

public class Waiter {
    private HandleOrder handler;
    private Map<String, MenuItem> menu = new HashMap<>();

    public Waiter(HandleOrder handler) {
        this.handler = handler;
    }

    public void placeOrder(int tableNumber, String[] itemDescriptions) {
        Order order = new Order(tableNumber);

        for (String item : itemDescriptions) {
            MenuItem menuItem = menu.get(item);
            // Check menuItem exists
//            Add to order

        }
        handler.handle(order);

    }

    private static class MenuItem {
        private final String name;
        private final double price;

        private MenuItem(String name, double price) {
            this.name = name;
            this.price = price;
        }
    }
}
