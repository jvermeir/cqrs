package restaurant.message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * User: mickdudley
 * Date: 16/12/2015
 */
public class AlarmClock implements MessageHandler, Startable {
    private final MessageBus bus;
    private List<DelayedSendMessage> messages = new ArrayList<>();
    private Thread myThread;

    public AlarmClock(MessageBus bus) {
        this.bus = bus;
    }


    @Override
    public void handle(OrderMessage message) {
        if (message instanceof DelayedSendMessage) {
            messages.add((DelayedSendMessage) message);
            Collections.sort(messages, new Comparator<DelayedSendMessage>() {
                @Override
                public int compare(DelayedSendMessage o1, DelayedSendMessage o2) {
                    return (o2.getEta() > o1.getEta()) ? 1 : -1;
                }
            });
        }

    }

    @Override
    public void start() {
        System.out.println("Starting AlarmClock ");
        myThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    for (DelayedSendMessage msg : messages) {
                        if (msg.getEta() > System.currentTimeMillis()) {
                            messages.remove(msg);
                            bus.publish(msg);
                        }
                        else {
                            break;
                        }
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
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
    public int getQueueCount() {
        return messages.size();
    }

    @Override
    public String getName() {
        return "Alarm Clock";
    }
}
