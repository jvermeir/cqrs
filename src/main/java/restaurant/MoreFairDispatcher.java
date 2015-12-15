package restaurant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jan on 15/12/15.
 */
public class MoreFairDispatcher implements HandleOrder  {

    private List<ThreadedHandler> handlers = new ArrayList<>();

    public MoreFairDispatcher(List<ThreadedHandler> handlers){
        this.handlers.addAll(handlers);
    }

    @Override
    public void handle(Order order) {
        while (true) {
            for (ThreadedHandler handleOrder : handlers) {
                if (handleOrder.getQueueCount() < 5) {
                    handleOrder.handle(order);
                    return;
                }
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
