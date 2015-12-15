package restaurant.message;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by jan on 15/12/15.
 */
public class RoundRobinDispatcher implements  MessageHandler {

    Queue<MessageHandler> messageQueue = new ConcurrentLinkedQueue<>();

    public RoundRobinDispatcher(List<MessageHandler> handlers){
        for (MessageHandler handler: handlers) {
            messageQueue.add(handler);
        }
    }

    @Override
    public void handle(OrderMessage message) {
        MessageHandler nextMessage = messageQueue.remove();
        nextMessage.handle(message);
        messageQueue.add(nextMessage);
    }

}

