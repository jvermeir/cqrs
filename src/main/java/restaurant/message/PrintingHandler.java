package restaurant.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PrintingHandler implements MessageHandler{
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void handle(OrderMessage message) {
        System.out.println(getClass().getSimpleName() + " handle");
        try {
            System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(message.getOrder()));
        } catch (JsonProcessingException e) {
            //
        }

    }
}
