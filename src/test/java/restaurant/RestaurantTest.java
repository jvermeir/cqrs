package restaurant;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

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
        cook = new Cook(asstManager);
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