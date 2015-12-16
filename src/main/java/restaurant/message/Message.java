package restaurant.message;

import java.util.UUID;

/**
 * User: mickdudley
 * Date: 15/12/2015
 */
public class Message {
    private UUID messageId;
    private UUID correlationId;
    private UUID causationId;

    public Message(Message message) {
        this.messageId = UUID.randomUUID();
        this.correlationId = message == null ? UUID.randomUUID() : message.getCorrelationId();
        this.causationId = message == null ? null : message.getMessageId();
    }

    public UUID getMessageId() {
        return messageId;
    }

    public UUID getCausationId() {
        return causationId;
    }

    public UUID getCorrelationId() {
        return correlationId;
    }
}
