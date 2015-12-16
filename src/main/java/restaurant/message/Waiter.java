package restaurant.message;

import com.google.common.collect.ImmutableMap;
import restaurant.MenuItem;
import restaurant.Order;

import java.util.ArrayList;
import java.util.List;

public class Waiter {
    private MessageBasedPubSub bus;
    ImmutableMap<String, MenuItem> menu = ImmutableMap.<String, MenuItem>builder()
            .put("pizza", new MenuItem("pizza", 10.0))
            .put("cake", new MenuItem("cake", 9.0))
            .put("razor blade pizza", new MenuItem("razor blade pizza", 9.99))
            .put("coke", new MenuItem("coke", 2.0))
            .build();

    public Waiter(MessageBasedPubSub bus) {
        this.bus = bus;
    }

    public void placeOrder(int tableNumber, String[] itemDescriptions) {
        Order order = new Order(tableNumber);
        List<MenuItem> items = new ArrayList<>();

        for (String item : itemDescriptions) {

            MenuItem orderItem = menu.get(item);
            if (orderItem != null) {
                orderItem.setQty(1);
                items.add(orderItem);
            }
            else {
                // Item is not on the menu
                throw new RuntimeException("No such menu item: " + item);
            }
            order.setItems(items);
        }
        OrderTracker orderTracker = new OrderTracker();
        OrderPlacedMessage message = new OrderPlacedMessage(order, null);
        bus.subscribe(message.getCorrelationId(), orderTracker);
        bus.publish(message);

    }

}
