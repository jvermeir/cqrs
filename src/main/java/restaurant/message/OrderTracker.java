package restaurant.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OrderTracker implements MessageHandler{
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void handle(OrderMessage message) {
        if (message instanceof OrderCookedMessage) {
            System.out.println("Foods ready");
        } else if (message instanceof  OrderPaidMessage) {
            System.out.println("Goodbye");
        }
    }
}
