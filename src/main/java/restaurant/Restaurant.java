package restaurant;

/**
 * User: mickdudley
 * Date: 14/12/2015
 */
public class Restaurant {

    public static void main(String args[]) {
        PrintingHandler printingHandler = new PrintingHandler();
        Cashier cashier = new Cashier(printingHandler);
        AsstManager asstManager = new AsstManager(cashier);
        Cook cook = new Cook(asstManager);
        Waiter waiter = new Waiter(cook);

        waiter.placeOrder(12, new String[] {"razor blade pizza"});
    }
}
