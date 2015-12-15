package restaurant.message;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by jan on 15/12/15.
 */
public class ThreadedMessageHandler implements Startable, MessageHandler {
    private Queue<OrderMessage> messageQueue = new ConcurrentLinkedQueue<>();

    public int getQueueCount() {
        return messageQueue.size();
    }

    private String name;
    public String getName() {
        return name;
    }

    private MessageHandler messageHandler;

    public ThreadedMessageHandler(MessageHandler messageHandler, String name) {
        this.messageHandler = messageHandler;
        this.name = name;
    }

    Thread myThread;

    @Override
    public void start() {
        System.out.println("Starting");
        myThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (messageQueue.peek() != null) {
                        OrderMessage message = messageQueue.remove();
                        System.out.println("got an order " + message);
                        messageHandler.handle(message);
                    } else {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    ;
                }
            }
        }
        );
        myThread.start();
    }

    @Override
    public void stop() {
        myThread.interrupt();
        try {
            myThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void handle(OrderMessage message) {
        System.out.println("queuing " + message + " in threadedhandler");
        messageQueue.add(message);
    }

}
