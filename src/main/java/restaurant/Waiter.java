package restaurant;

import com.google.common.collect.ImmutableMap;

import java.util.ArrayList;
import java.util.List;

public class Waiter {
    private HandleOrder handler;
    ImmutableMap<String, MenuItem> menu = ImmutableMap.<String, MenuItem>builder()
        .put("pizza", new MenuItem("pizza", 10.0))
        .put("cake", new MenuItem("cake", 9.0))
        .put("coke", new MenuItem("coke", 2.0))
        .build();

    public Waiter(HandleOrder handler) {
        this.handler = handler;
    }

    public void placeOrder(int tableNumber, String[] itemDescriptions) {
        Order order = new Order(tableNumber);
        List<MenuItem> items = new ArrayList<>();

        for (String item : itemDescriptions) {

            MenuItem orderItem = menu.get(item);
            if (orderItem != null) {
                // Add to order
                orderItem.setQty(1);
                items.add(orderItem);
            }
            else {
                // Item is not on the menu
                throw new RuntimeException("No such menu item: " + item);
            }
            order.setItems(items);
        }
        handler.handle(order);

    }

}
