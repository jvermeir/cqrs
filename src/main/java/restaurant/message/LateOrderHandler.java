package restaurant.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import restaurant.Order;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class LateOrderHandler implements MessageHandler{
    private final ObjectMapper mapper = new ObjectMapper();
    private List<Order> lateOrders = new ArrayList<>();

    public List<Order> getLateOrders() {
        return lateOrders;
    }

    @Override
    public void handle(OrderMessage message) {
        System.out.println("Adding " + message.getCorrelationId() + " to late orders");
        lateOrders.add(message.getOrder());
    }
}
