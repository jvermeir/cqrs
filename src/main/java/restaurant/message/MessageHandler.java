package restaurant.message;

/**
 * User: mickdudley
 * Date: 14/12/2015
 */
public interface MessageHandler {
    void handle(OrderMessage message);
}
