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
        Cook cook = new Cook(asstManager, "Cook 1");
        Waiter waiter = new Waiter(cook);

        waiter.placeOrder(12, new String[] {"razor blade pizza"});

        for (String menu : waiter.menu.keySet()) {
            int tableNo = Math.toIntExact(Math.round(Math.random()*10));
            waiter.placeOrder(tableNo, new String[] {menu});
        }
    }
}
