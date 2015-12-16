package restaurant.message;

import org.junit.Before;
import org.junit.Test;
import restaurant.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

/**
 * User: mickdudley
 * Date: 16/12/2015
 */
public class ProcessManagerFactoryTest {
    MockBus bus;
    ProcessManagerFactory factory;

    @Before
    public void setUp() throws Exception {
        bus = new MockBus();
        factory = new ProcessManagerFactory(bus);
    }

    @Test
    public void testCreatesProcessManagersForOrder() throws Exception {
        factory.handle(new OrderPlacedMessage(new Order(123), null));
        assertEquals(1, factory.getProcessManagers().size());
    }

    @Test
    public void testCreatesProcessManagersAreRemoved() throws Exception {
        OrderMessage message = new OrderPlacedMessage(new Order(123), null);
        factory.handle(message);

        factory.handle(new OrderCompleteMessage(new Order(123), message));

        assertEquals(0, factory.getProcessManagers().size());
    }

    @Test
    public void testCreatesDodgyPM() {
        Order dodgyOrder = new Order(123);
        dodgyOrder.setDodgy(true);
        boolean ans = dodgyOrder.getDodgy();

        Order standardOrder = new Order(123);

        OrderPlacedMessage dodgyMessage = new OrderPlacedMessage(dodgyOrder, null);
        factory.handle(dodgyMessage);
        OrderPlacedMessage standardMessage = new OrderPlacedMessage(standardOrder, null);
        factory.handle(standardMessage);
        assertEquals(2, factory.getProcessManagers().size());

        assertEquals(StandardProcessManager.class, factory.getProcessManagers().get(dodgyMessage.getCorrelationId()).getClass());
        assertEquals(PrePayProcessManager.class, factory.getProcessManagers().get(standardMessage.getCorrelationId()).getClass());

    }


    class MockBus implements MessageBus {
        List<Message> publishedMessages = new ArrayList<>();

        public List<Message> getPublishedMessages() {
            return publishedMessages;
        }

        @Override
        public <T> void subscribe(Class klass, MessageHandler handler) {

        }

        @Override
        public <T> void subscribe(UUID uuid, MessageHandler handler) {

        }

        @Override
        public void publish(OrderMessage message) {
            publishedMessages.add(message);
        }
    }

}