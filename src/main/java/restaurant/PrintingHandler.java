package restaurant;

import com.fasterxml.jackson.core.JsonProcessingException;

public class PrintingHandler implements HandleOrder{

    @Override
    public void handle(Order order) {
        System.out.println(getClass().getSimpleName() + " handle");
        try {
            System.out.println(order.print());
        } catch (JsonProcessingException e) {
            //
        }

    }
}
