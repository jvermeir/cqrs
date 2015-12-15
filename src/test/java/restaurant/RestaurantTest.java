package restaurant;

import com.google.common.collect.ImmutableList;
import com.sun.tools.javadoc.Start;
import org.junit.Before;
import org.junit.Test;

import java.util.logging.StreamHandler;

import static org.junit.Assert.*;

public class RestaurantTest {

    private PrintingHandler printingHandler;
    private OrderCaptureHandler orderCaptureHandler;
    private Cashier cashier;
    private AsstManager asstManager;
    private Cook cook;
    private Waiter waiter;

    @Before
    public void setUp() throws Exception {
        printingHandler = new PrintingHandler();
        orderCaptureHandler = new OrderCaptureHandler(printingHandler);
        cashier = new Cashier(orderCaptureHandler);
        asstManager = new AsstManager(cashier);
        cook = new Cook(asstManager, "name");
        waiter = new Waiter(cook);
    }

    @Test
    public void testWaiterCanPlaceOrder() {
        // given
        waiter.placeOrder(12, new String[]{"razor blade pizza"});
        // when
        Order order = orderCaptureHandler.getOrder();
        // then
        assertEquals(12, order.getTableNumber());
    }

    @Test
    public void testMultiPlexer() {
        // given
        ImmutableList<HandleOrder> handleOrders = ImmutableList.<HandleOrder>builder()
                .add(new Cook(asstManager, "cook1"))
                .add(new Cook(asstManager, "cook2"))
                .add(new Cook(asstManager, "cook3"))
                .build();
        MultiPlexer multiPlexer = new MultiPlexer(handleOrders);
        Waiter waiter = new Waiter(multiPlexer);
        // when
        waiter.placeOrder(12, new String[]{"razor blade pizza"});
        Order order = orderCaptureHandler.getOrder();
        // then
        assertEquals(12, order.getTableNumber());
        assertEquals(4, cashier.getPaidOrders().size());
    }

    @Test
    public void testRoundRobinDispatcher() {
        // given
        ImmutableList<HandleOrder> handleOrders = ImmutableList.<HandleOrder>builder()
                .add(new Cook(asstManager, "cook1"))
                .add(new Cook(asstManager, "cook2"))
                .add(new Cook(asstManager, "cook3"))
                .build();
        RoundRobinDispatcher roundRobinDispatcher = new RoundRobinDispatcher(handleOrders);
        Waiter waiter = new Waiter(roundRobinDispatcher);
        // when
        waiter.placeOrder(12, new String[]{"razor blade pizza"});
        waiter.placeOrder(12, new String[]{"pizza"});
        waiter.placeOrder(12, new String[]{"coke"});
        waiter.placeOrder(12, new String[]{"cake"});
        Order order = orderCaptureHandler.getOrder();
        // then
        assertEquals(12, order.getTableNumber());
        assertEquals(4, cashier.getPaidOrders().size());
    }

    @Test
    public void testThreadedRoundRobinDispatcher() throws  Exception{
        // given
        ImmutableList<HandleOrder> handleOrders = ImmutableList.<HandleOrder>builder()
                .add(new ThreadedHandler(new Cook(asstManager, "cook1")))
                .add(new ThreadedHandler(new Cook(asstManager, "cook2")))
                .add(new ThreadedHandler(new Cook(asstManager, "cook3")))
                .build();
        RoundRobinDispatcher roundRobinDispatcher = new RoundRobinDispatcher(handleOrders);
        for(HandleOrder handleOrder: handleOrders) {
            Startable startable = (Startable) handleOrder;
            startable.start();
        }
        Waiter waiter = new Waiter(roundRobinDispatcher);
        // when
        waiter.placeOrder(12, new String[]{"razor blade pizza"});
        waiter.placeOrder(12, new String[]{"pizza"});
        waiter.placeOrder(12, new String[]{"coke"});
        waiter.placeOrder(12, new String[]{"cake"});
        Thread.sleep(5000);
        // then
        assertEquals(4, cashier.getPaidOrders().size());
    }

    public static class OrderCaptureHandler implements HandleOrder{
        private final HandleOrder handler;
        private Order savedOrder;

        public OrderCaptureHandler(HandleOrder handler) {
            this.handler = handler;
        }

        @Override
        public void handle(Order order) {
            savedOrder = order;
            handler.handle(order);
        }

        public Order getOrder() {
            return savedOrder;
        }
    }

}