package restaurant;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PrintingHandler implements HandleOrder{
    private final ObjectMapper mapper = new ObjectMapper();


    @Override
    public void handle(Order order) {
        System.out.println(getClass().getSimpleName() + " handle");
        try {
            System.out.println(mapper.writeValueAsString(order));
        } catch (JsonProcessingException e) {
            //
        }

    }
}
