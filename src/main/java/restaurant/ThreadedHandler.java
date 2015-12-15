package restaurant;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by jan on 15/12/15.
 */
public class ThreadedHandler implements Startable, HandleOrder {
    private HandleOrder handleOrder;
    public ThreadedHandler(HandleOrder handleOrder) {
        this.handleOrder = handleOrder;
    }

    Thread myThread;
    public void start() {
        System.out.println("Starting");
        myThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (orderQueue.peek()!=null) {
                        Order order = orderQueue.remove();
                        System.out.println("got an order " + order);
                        handleOrder.handle(order);
                    } else {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    };
                }
            }
        }
        ) ;
        myThread.start();
    }
    public  void stop() {
        myThread.interrupt();
        try {
            myThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    Queue<Order> orderQueue = new ConcurrentLinkedQueue<>();

    @Override
    public void handle(Order order) {
        System.out.println("queuing " + order + " in threadedhandler");
        orderQueue.add(order);
    }

}
