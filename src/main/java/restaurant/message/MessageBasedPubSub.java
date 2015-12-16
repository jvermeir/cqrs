package restaurant.message;

import restaurant.HandleOrder;
import restaurant.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User: mickdudley
 * Date: 15/12/2015
 */
public class MessageBasedPubSub {
    Map<String, List<MessageHandler>> subscribers = new ConcurrentHashMap<>();

    public <T> void subscribe(Class klass, MessageHandler handler) {
        String keyName = klass.getCanonicalName();
        subscribe(keyName, handler);
    }
    public <T> void subscribe(UUID uuid, MessageHandler handler) {
        String keyName = uuid.toString();
        subscribe(keyName, handler);
    }

    private <T> void subscribe(String key, MessageHandler handler) {
        List<MessageHandler> handlers = subscribers.get(key);
        if (handlers == null) {
            handlers = new ArrayList<>();
        }
        handlers.add(handler);
        subscribers.put(key, handlers);
    }

    public void publish(OrderMessage message) {
        publish(message, message.getClass().getCanonicalName());
        publish(message, message.getCorrelationId().toString());
    }

    private void publish(OrderMessage message, String key) {
        List<MessageHandler> handlers = subscribers.get(key);
        if (handlers != null) {
            for (MessageHandler handler : handlers) {
                handler.handle(message);
            }
        }
    }
/*
    public class WidenHandler<T> implements MessageHandler<Message> {
        private MessageHandler<T> handler;

        public WidenHandler(MessageHandler<T> handler) {
            this.handler = handler;
        }

        @Override
        public void handle(Message message) {
            try {
                T msg = (T) message;
                handler.handle(msg);
            }
            catch (Exception e) {
                // ignore
            }
        }
    }
*/
}
