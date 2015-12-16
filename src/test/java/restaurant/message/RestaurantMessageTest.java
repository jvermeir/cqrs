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
        bus.subscribe(PayOrderMessage.class, cashierHandler);
        ThreadedMessageHandler asstManagerHandler = new ThreadedMessageHandler(new AsstManager(bus), "assistant manager");
        bus.subscribe(PriceOrderMessage.class, asstManagerHandler);

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

        bus.subscribe(CookOrderMessage.class, kitchen);
        Waiter waiter = new Waiter(bus);
        kitchen.start();
        Router router = new Router(bus);

        // when

        int maxOrders = 100;
        addRandomOrders(waiter, maxOrders);

        // then
        waitForStuffToFinish(cashier, maxOrders);
    }

    private void addRandomOrders(Waiter waiter, int number) {
        String[] dishes = new String[]{"razor blade pizza", "pizza", "coke", "cake"};

        for (int i=0;i<number ; i++) {
            int tableNo = Math.toIntExact(Math.round(Math.random() * 10));
            waiter.placeOrder(tableNo, new String[] {dishes[i % 4]});
        }

    }

    private void waitForStuffToFinish(Cashier cashier, int maxOrders) throws InterruptedException {
        boolean stop = false;
        while (!stop) {
            Thread.sleep(500);
            System.out.println ("Number of messages processed: " + cashier.getPaidOrders().size());
            if (cashier.getPaidOrders().size() == maxOrders) {
                stop = true;
            }
        }
    }

}