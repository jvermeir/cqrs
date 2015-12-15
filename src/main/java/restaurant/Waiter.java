package restaurant;

import com.google.common.collect.ImmutableMap;

import java.util.ArrayList;
import java.util.List;

public class Waiter {
    private TopicBasedPubSub bus;
    private HandleOrder handler;
    ImmutableMap<String, MenuItem> menu = ImmutableMap.<String, MenuItem>builder()
            .put("pizza", new MenuItem("pizza", 10.0))
            .put("cake", new MenuItem("cake", 9.0))
            .put("razor blade pizza", new MenuItem("razor blade pizza", 9.99))
            .put("coke", new MenuItem("coke", 2.0))
            .build();

    public Waiter(HandleOrder handler) {
        this.handler = handler;
    }


    public Waiter(TopicBasedPubSub bus) {
        this.bus = bus;
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
        if (bus == null) {
            handler.handle(order);
        }
        else {
            bus.publish("orderReceived", order);
        }

    }

}
