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
    MockBus bus;

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
        List<Message> publishedMessages = bus.getPublishedMessages();
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
        List<Message> publishedMessages = bus.getPublishedMessages();
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
        List<Message> publishedMessages = bus.getPublishedMessages();
        assertEquals(1, publishedMessages.size());
        Message message1 = publishedMessages.get(0);
        assertEquals(PayOrderMessage.class, message1.getClass());
    }

    @Test
    public void testRecookOrderIfTimeout() {
        // given
        Order order = new Order(42);
        OrderPlacedMessage orderPlacedMessage = new OrderPlacedMessage(order, null);
        standardProcessManager.handle(orderPlacedMessage);
        bus.clear();
        // when
        standardProcessManager.handle(new CookingTimedOutMessage(order, orderPlacedMessage, 1));
        List<Message> publishedMessages = bus.getPublishedMessages();
        Message message1 = publishedMessages.get(0);
        assertEquals(CookOrderMessage.class, message1.getClass());
    }

    @Test
    public void testDontRecookOrderIfTimeoutButOrderCooked() {
        // given
        Order order = new Order(42);
        OrderPlacedMessage orderPlacedMessage = new OrderPlacedMessage(order, null);
        standardProcessManager.handle(orderPlacedMessage);
        OrderCookedMessage orderCookedMessage = new OrderCookedMessage(order, orderPlacedMessage);
        standardProcessManager.handle(orderCookedMessage);
        bus.clear();
        // when
        standardProcessManager.handle(new CookingTimedOutMessage(order, orderPlacedMessage, 1));
        // then
        List<Message> publishedMessages = bus.getPublishedMessages();
        assertEquals(0, publishedMessages.size());
    }

    @Test
    public void testDropOrderIfTryToReCookMoreThanThreeTimes() {
        // given
        Order order = new Order(42);
        OrderPlacedMessage orderPlacedMessage = new OrderPlacedMessage(order, null);
        standardProcessManager.handle(orderPlacedMessage);
        bus.clear();
        // when
        standardProcessManager.handle(new CookingTimedOutMessage(order, orderPlacedMessage, 4));
        // then
        List<Message> publishedMessages = bus.getPublishedMessages();
        assertEquals(1, publishedMessages.size());
        Message message1 = publishedMessages.get(0);
        assertEquals(OrderDroppedMessage.class, message1.getClass());
    }


    @Test
    public void testOrderPaid() {
        // given
        Order order = new Order(42);
        OrderPaidMessage message = new OrderPaidMessage(order, null);
        // when
        standardProcessManager.handle(message);
        // then
        List<Message> publishedMessages = bus.getPublishedMessages();
        assertEquals(1, publishedMessages.size());
        Message message1 = publishedMessages.get(0);
        assertEquals(OrderCompleteMessage.class, message1.getClass());
    }


    class MockBus implements MessageBus {
        List<Message> publishedMessages = new ArrayList<>();

        public List<Message> getPublishedMessages() {
            return publishedMessages;
        }

        public void clear() {
            publishedMessages.clear();
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
