package restaurant.message;

import com.google.common.collect.ImmutableList;
import jdk.nashorn.internal.runtime.ECMAException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class RestaurantMessageTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testMoreFairDispatcherWithOrderDropperAndBus() throws  Exception{
        // given
        PrintingHandler printingHandler = new PrintingHandler();

        MessageBasedPubSub bus = new MessageBasedPubSub();

        bus.subscribe(OrderPaidMessage.class, printingHandler);

        Cashier cashier = new Cashier(bus);
        ThreadedMessageHandler cashierHandler = new ThreadedMessageHandler(cashier, "cashier1");
        bus.subscribe(OrderPricedMessage.class, cashierHandler);
        ThreadedMessageHandler asstManagerHandler = new ThreadedMessageHandler(new AsstManager(bus), "assistant manager");
        bus.subscribe(OrderCookedMessage.class, asstManagerHandler);

        ImmutableList<ThreadedMessageHandler> cookHandlers = ImmutableList.<ThreadedMessageHandler>builder()
                .add(new ThreadedMessageHandler(new Cook("cook1", bus), "cook1queue"))
                .add(new ThreadedMessageHandler(new Cook("cook2", bus), "cook2queue"))
                .add(new ThreadedMessageHandler(new Cook("cook3", bus), "cook3queue"))
                .build();

        List<Startable> startables = new ArrayList<>();
        for (ThreadedMessageHandler handler : cookHandlers) {
            startables.add((Startable) handler);
        }
        startables.add((Startable) cashierHandler);
        startables.add((Startable) asstManagerHandler);

        MoreFairDispatcher moreFairDispatcher = new MoreFairDispatcher(cookHandlers);
        for (Startable startable:startables) {
            startable.start();
        }
        ThreadedMessageHandler kitchen = new ThreadedMessageHandler(moreFairDispatcher, "Fair cook dispatcher");

        bus.subscribe(OrderPlacedMessage.class, kitchen);
        Waiter waiter = new Waiter(bus);
        kitchen.start();
        // when

        int maxOrders = 100;
        addRandomOrders(waiter, maxOrders);

        // then
        waitForStuffToFinish(cookHandlers, kitchen, maxOrders);
        System.out.println("Processed " + cashier.getPaidOrders().size() + " orders");
        assertEquals(100, cashier.getPaidOrders().size());
    }

    private void addRandomOrders(Waiter waiter, int number) {
        String[] dishes = new String[]{"razor blade pizza", "pizza", "coke", "cake"};

        for (int i=0;i<number ; i++) {
            int tableNo = Math.toIntExact(Math.round(Math.random() * 10));
            waiter.placeOrder(tableNo, new String[] {dishes[i % 4]});
        }

    }

    private void waitForStuffToFinish(ImmutableList<ThreadedMessageHandler> messageHandlers, ThreadedMessageHandler cookDispatcher, int maxOrders) throws InterruptedException {
        int counts = 1;
        while (counts > 0) {
            Thread.sleep(500);
            counts = 0;
            for(MessageHandler messageHandler: messageHandlers) {
                Startable startable = (Startable) messageHandler;
                System.out.println(startable.getName() + ": " + startable.getQueueCount());
                counts += startable.getQueueCount();
            }
            System.out.println(cookDispatcher.getName() + ": " + cookDispatcher.getQueueCount());
            counts += cookDispatcher.getQueueCount();
        }
    }

}