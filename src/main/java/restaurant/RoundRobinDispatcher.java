package restaurant;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by jan on 15/12/15.
 */
public class RoundRobinDispatcher implements  HandleOrder {

    Queue<HandleOrder> handlerQueue = new ConcurrentLinkedQueue<>();

    public RoundRobinDispatcher(List<HandleOrder> handlers){
        for (HandleOrder handler: handlers) {
            handlerQueue.add(handler);
        }
    }

    @Override
    public void handle(Order order) {
        HandleOrder handleOrder = handlerQueue.remove();
        handleOrder.handle(order);
        handlerQueue.add(handleOrder);
    }

}

