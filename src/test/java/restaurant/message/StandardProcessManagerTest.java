package restaurant.message;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import restaurant.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class StandardProcessManagerTest {

    StandardProcessManager standardProcessManager;
    MessageBus bus;
    @Before
    public void setup() {
        bus = new MockBus();
        standardProcessManager = new StandardProcessManager(bus);
        bus.subscribe(OrderPlacedMessage.class, standardProcessManager);
    }

    @Test
    public void testOrderCreated() {
        // given
        Order order = new Order(42);
        OrderPlacedMessage message = new OrderPlacedMessage(order, null);
        // when
        standardProcessManager.handle(message);
        // then
        List<Message> publishedMessages = ((MockBus) bus).getPublishedMessages();
        Message message1 = publishedMessages.get(0);
        assertEquals(CookOrderMessage.class, message1.getClass());
    }

    @Test
    public void testOrderCooked() {
        // given
        Order order = new Order(42);
        OrderCookedMessage message = new OrderCookedMessage(order, null);
        // when
        standardProcessManager.handle(message);
        // then
        List<Message> publishedMessages = ((MockBus) bus).getPublishedMessages();
        assertEquals(1, publishedMessages.size());
        Message message1 = publishedMessages.get(0);
        assertEquals(PriceOrderMessage.class, message1.getClass());
    }

    @Test
    public void testOrderPriced() {
        // given
        Order order = new Order(42);
        OrderPricedMessage message = new OrderPricedMessage(order, null);
        // when
        standardProcessManager.handle(message);
        // then
        List<Message> publishedMessages = ((MockBus) bus).getPublishedMessages();
        assertEquals(1, publishedMessages.size());
        Message message1 = publishedMessages.get(0);
        assertEquals(PayOrderMessage.class, message1.getClass());
    }

    @Test
    public void testOrderPaid() {
        // given
        Order order = new Order(42);
        OrderPaidMessage message = new OrderPaidMessage(order, null);
        // when
        standardProcessManager.handle(message);
        // then
        List<Message> publishedMessages = ((MockBus) bus).getPublishedMessages();
        assertEquals(1, publishedMessages.size());
        Message message1 = publishedMessages.get(0);
        assertEquals(OrderCompleteMessage.class, message1.getClass());
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
