package restaurant.message;

import java.util.UUID;

/**
 * Created by jan on 16/12/15.
 */
public interface MessageBus {
    <T> void subscribe(Class klass, MessageHandler handler);

    <T> void subscribe(UUID uuid, MessageHandler handler);

    void publish(OrderMessage message);
}
