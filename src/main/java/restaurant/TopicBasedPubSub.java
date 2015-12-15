package restaurant;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User: mickdudley
 * Date: 15/12/2015
 */
public class TopicBasedPubSub {
    Map<String, List<HandleOrder>> subscribers = new ConcurrentHashMap<>();

    public void subscribe(String topic, HandleOrder handler) {
        List<HandleOrder> handlers = subscribers.get(topic);
        if (handlers == null) {
            handlers = new ArrayList<>();
        }
        handlers.add(handler);
        subscribers.put(topic, handlers);
    }

    public void publish(String topic, Order order) {
        List<HandleOrder> handlers = subscribers.get(topic);
        if (handlers != null) {
            for (HandleOrder handler : handlers) {
                handler.handle(order);
            }
        }

    }
}
