package restaurant.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.UUID;

public class OrderTracker implements MessageHandler{
    private final ObjectMapper mapper = new ObjectMapper();

    private final UUID uuid = UUID.randomUUID();
    @Override
    public void handle(OrderMessage message) {
        if (message instanceof OrderCookedMessage) {
            System.out.println(uuid + " Foods ready: " + message.getCorrelationId());
        } else if (message instanceof  OrderPaidMessage) {
            System.out.println(uuid + " Goodbye: " + message.getCorrelationId());
        }
    }
}
