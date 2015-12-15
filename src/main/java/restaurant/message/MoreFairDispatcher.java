package restaurant.message;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by jan on 15/12/15.
 */
public class MoreFairDispatcher implements MessageHandler  {

    private List<ThreadedMessageHandler> handlers = new ArrayList<>();

    public MoreFairDispatcher(List<ThreadedMessageHandler> handlers){
        this.handlers.addAll(handlers);
    }

    @Override
    public void handle(OrderMessage message) {
        while (true) {
            for (ThreadedMessageHandler handleMessage : handlers) {
                if (handleMessage.getQueueCount() < 5) {
                    handleMessage.handle(message);
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
