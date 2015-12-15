package restaurant.message;

import restaurant.HandleOrder;
import restaurant.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User: mickdudley
 * Date: 15/12/2015
 */
public class MessageBasedPubSub {
    Map<String, List<MessageHandler>> subscribers = new ConcurrentHashMap<>();

    public <T> void subscribe(Class klass, MessageHandler handler) {
        String keyName = klass.getCanonicalName();
        List<MessageHandler> handlers = subscribers.get(keyName);
        if (handlers == null) {
            handlers = new ArrayList<>();
        }
        handlers.add(handler);
        subscribers.put(keyName, handlers);
    }

    public void publish(OrderMessage message) {
        List<MessageHandler> handlers = subscribers.get(message.getClass().getCanonicalName());
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
