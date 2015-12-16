package restaurant;

import com.google.common.collect.ImmutableList;
import com.sun.tools.javadoc.Start;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
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

    private void addRandomOrders(Waiter waiter, int number) {
        String[] dishes = new String[]{"razor blade pizza", "pizza", "coke", "cake"};

        for (int i=0;i<number ; i++) {
            int tableNo = Math.toIntExact(Math.round(Math.random() * 10));
            waiter.placeOrder(tableNo, new String[] {dishes[i % 4]});
        }

    }

    @Test
    public void testThreadedRoundRobinDispatcher() throws  Exception{
        // given
        ImmutableList<HandleOrder> handleOrders = ImmutableList.<HandleOrder>builder()
                .add(new ThreadedHandler(new Cook(asstManager, "cook1"),"cook1queue"))
                .add(new ThreadedHandler(new Cook(asstManager, "cook2"),"cook2queue"))
                .add(new ThreadedHandler(new Cook(asstManager, "cook3"),"cook3queue"))
                .build();
        RoundRobinDispatcher roundRobinDispatcher = new RoundRobinDispatcher(handleOrders);
        for(HandleOrder handleOrder: handleOrders) {
            Startable startable = (Startable) handleOrder;
            startable.start();
        }
        Waiter waiter = new Waiter(roundRobinDispatcher);
        // when

        int maxOrders = 100;
        addRandomOrders(waiter, maxOrders);

        // then
        while (cashier.getPaidOrders().size()!=maxOrders) {
            for(HandleOrder handleOrder: handleOrders) {
                Startable startable = (Startable) handleOrder;
                System.out.println(startable.getName() + ": " + startable.getQueueCount());
            }
            Thread.sleep(500);
        }
        assertEquals(maxOrders, cashier.getPaidOrders().size());
    }

    @Test
    public void testMoreFairDispatcher() throws  Exception{
        // given

//        printingHandler = new PrintingHandler();
//        cashier = new Cashier(orderCaptureHandler);
//        asstManager = new AsstManager(cashier);
//        cook = new Cook(asstManager, "name");
//        waiter = new Waiter(cook);

        cashier = new Cashier(printingHandler);
        ThreadedHandler cashierHandler = new ThreadedHandler(cashier, "cashier1");
        ThreadedHandler asstManagerHandler = new ThreadedHandler(new AsstManager(cashierHandler), "assistant manager");

        ImmutableList<ThreadedHandler> cookHandlers = ImmutableList.<ThreadedHandler>builder()
                .add(new ThreadedHandler(new Cook(asstManagerHandler, "cook1"),"cook1queue"))
                .add(new ThreadedHandler(new Cook(asstManagerHandler, "cook2"),"cook2queue"))
                .add(new ThreadedHandler(new Cook(asstManagerHandler, "cook3"),"cook3queue"))
                .build();

        List<Startable> startables = new ArrayList<>();
        startables.addAll(cookHandlers);
        startables.add(cashierHandler);
        startables.add(asstManagerHandler);

        MoreFairDispatcher moreFairDispatcher = new MoreFairDispatcher(cookHandlers);
        for (Startable startable:startables) {
            startable.start();
        }
        ThreadedHandler cookDispatcher = new ThreadedHandler(moreFairDispatcher, "Fair cook dispatcher");
        Waiter waiter = new Waiter(cookDispatcher);
        cookDispatcher.start();
        // when

        int maxOrders = 100;
        addRandomOrders(waiter, maxOrders);

        // then
        waitForStuffToFinish(cookHandlers, cookDispatcher, maxOrders);
        assertEquals(maxOrders, cashier.getPaidOrders().size());
    }


    @Test
    public void testMoreFairDispatcherWithOrderDropper() throws  Exception{
        // given

//        printingHandler = new PrintingHandler();
//        cashier = new Cashier(orderCaptureHandler);
//        asstManager = new AsstManager(cashier);
//        cook = new Cook(asstManager, "name");
//        waiter = new Waiter(cook);

        cashier = new Cashier(printingHandler);
        ThreadedHandler cashierHandler = new ThreadedHandler(cashier, "cashier1");
        ThreadedHandler asstManagerHandler = new ThreadedHandler(new AsstManager(cashierHandler), "assistant manager");

        ImmutableList<ThreadedHandler> cookHandlers = ImmutableList.<ThreadedHandler>builder()
                .add(new ThreadedHandler(new TTLHandler(new Cook(asstManagerHandler, "cook1")), "cook1queue"))
                .add(new ThreadedHandler(new TTLHandler(new Cook(asstManagerHandler, "cook2")), "cook2queue"))
                .add(new ThreadedHandler(new TTLHandler(new Cook(asstManagerHandler, "cook3")), "cook3queue"))
                .build();

        List<Startable> startables = new ArrayList<>();
        startables.addAll(cookHandlers);
        startables.add(cashierHandler);
        startables.add(asstManagerHandler);

        MoreFairDispatcher moreFairDispatcher = new MoreFairDispatcher(cookHandlers);
        for (Startable startable:startables) {
            startable.start();
        }
        ThreadedHandler cookDispatcher = new ThreadedHandler(moreFairDispatcher, "Fair cook dispatcher");
        Waiter waiter = new Waiter(cookDispatcher);
        cookDispatcher.start();
        // when

        int maxOrders = 100;
        addRandomOrders(waiter, maxOrders);

        // then
        waitForStuffToFinish(cookHandlers, cookDispatcher, maxOrders);
        System.out.println("Processed " + cashier.getPaidOrders().size() + " orders");

//        assertEquals(maxOrders, cashier.getLateOrders().size());
    }


    @Test
    public void testMoreFairDispatcherWithOrderDropperAndBus() throws  Exception{
        // given

//        printingHandler = new PrintingHandler();
//        cashier = new Cashier(orderCaptureHandler);
//        asstManager = new AsstManager(cashier);
//        cook = new Cook(asstManager, "name");
//        waiter = new Waiter(cook);

        TopicBasedPubSub bus = new TopicBasedPubSub();

        bus.subscribe("orderPaid", printingHandler);
        cashier = new Cashier(bus);
        ThreadedHandler cashierHandler = new ThreadedHandler(cashier, "cashier1");
        bus.subscribe("orderPayable", cashierHandler);
        ThreadedHandler asstManagerHandler = new ThreadedHandler(new AsstManager(bus), "assistant manager");
        bus.subscribe("orderCooked", asstManagerHandler);

        ImmutableList<ThreadedHandler> cookHandlers = ImmutableList.<ThreadedHandler>builder()
                .add(new ThreadedHandler(new TTLHandler(new Cook("cook1", bus)), "cook1queue"))
                .add(new ThreadedHandler(new TTLHandler(new Cook("cook2", bus)), "cook2queue"))
                .add(new ThreadedHandler(new TTLHandler(new Cook("cook3", bus)), "cook3queue"))
                .build();

        List<Startable> startables = new ArrayList<>();
        startables.addAll(cookHandlers);
        startables.add(cashierHandler);
        startables.add(asstManagerHandler);

        MoreFairDispatcher moreFairDispatcher = new MoreFairDispatcher(cookHandlers);
        for (Startable startable:startables) {
            startable.start();
        }
        ThreadedHandler kitchen = new ThreadedHandler(moreFairDispatcher, "Fair cook dispatcher");
        bus.subscribe("orderReceived", kitchen);
        Waiter waiter = new Waiter(bus);
        kitchen.start();
        // when

        int maxOrders = 100;
        addRandomOrders(waiter, maxOrders);

        // then
        waitForStuffToFinish(cookHandlers, kitchen, maxOrders);
        System.out.println("Processed " + cashier.getPaidOrders().size() + " orders");

//        assertEquals(maxOrders, cashier.getLateOrders().size());
    }




    private void waitForStuffToFinish(ImmutableList<ThreadedHandler> handleOrders, ThreadedHandler cookDispatcher, int maxOrders) throws InterruptedException {
        int counts = 1;
        while (counts > 0) {
            Thread.sleep(500);
            counts = 0;
            for(HandleOrder handleOrder: handleOrders) {
                Startable startable = (Startable) handleOrder;
                System.out.println(startable.getName() + ": " + startable.getQueueCount());
                counts += startable.getQueueCount();
            }
            System.out.println(cookDispatcher.getName() + ": " + cookDispatcher.getQueueCount());
            counts += cookDispatcher.getQueueCount();
        }
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