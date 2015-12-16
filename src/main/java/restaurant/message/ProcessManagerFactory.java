package restaurant.message;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by jan on 16/12/15.
 */
public class ProcessManagerFactory implements MessageHandler {
    private Map<UUID, ProcessManager> processManagers = new HashMap<>();

    private final MessageBus bus;

    public ProcessManagerFactory(MessageBus bus) {
        this.bus = bus;
        bus.subscribe(OrderPaidMessage.class, this);
    }

    @Override
    public void handle(OrderMessage message) {
        if (message instanceof OrderPlacedMessage) {
            ProcessManager processManager = new ProcessManager(bus);
            processManagers.put(message.getCorrelationId(), processManager);
            bus.subscribe(message.getCorrelationId(), processManager);
        } else if (message instanceof OrderPaidMessage) {
            processManagers.remove(message.getCorrelationId());
        }
    }

}
