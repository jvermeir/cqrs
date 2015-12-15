package restaurant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jan on 15/12/15.
 */
public class MultiPlexer implements  HandleOrder {

    private List<HandleOrder> handlers;
    public MultiPlexer(List<HandleOrder> handlers){
        this.handlers = new ArrayList<HandleOrder>();
        this.handlers.addAll(handlers);
    }

    @Override
    public void handle(Order order) {
        for (HandleOrder handler:handlers) {
            handler.handle(order);
        }
    }
}
