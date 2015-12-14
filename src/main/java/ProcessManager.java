import java.util.ArrayList;
import java.util.List;

/**
 * Created by jan on 14/12/15.
 */
public class ProcessManager {
    List<Event> sevenSecondsList = new ArrayList<Event>();
    List<Event> elevenSecondsList = new ArrayList<Event>();
    private long triggerPrice = 0L;
    private Publisher publisher;
    private enum States { OPEN, SELLING, CLOSED }
    private States state = States.OPEN;

    public ProcessManager(Publisher publisher) {
        this.publisher = publisher;
    }

    public void handlePriceTickOccured(PriceTickOccured priceTickOccuredEvent) {
        sevenSecondsList.add(priceTickOccuredEvent);
        sendToSelfInXSecondsMessage(7L, new RemoveFromSevenSecondsQueueCommand(priceTickOccuredEvent));
        elevenSecondsList.add(priceTickOccuredEvent);
        updateTriggerPrice();
    }

    private void sendToSelfInXSecondsMessage(long interval, RemoveFromSevenSecondsQueueCommand removeFromSevenSecondsQueueCommand) {
        System.out.println("Sending in " + interval + " seconds " + removeFromSevenSecondsQueueCommand.getTheEvent());
    }

    public void handleRemoveFromSevenSecondQueue(Event event) {
        sevenSecondsList.remove(event);
        updateTriggerPrice();
    }

    public void handleRemoveFromElevenSecondQueue(Event event) {
        elevenSecondsList.remove(event);
        triggerSell();
    }

    private void triggerSell () {
        if (state==States.OPEN) {
            long maxPrice = -1L;
            for (Event event : elevenSecondsList) {
                if (event.getPrice() > maxPrice) {
                    maxPrice = event.getPrice();
                }
            }
            if (maxPrice < triggerPrice) {
                publisher.publish(new TriggerSellOccuredEvent());
                state = States.SELLING;
            }
        }
    }

    private void updateTriggerPrice () {
        long minPrice = 99999999999L;
        for (Event event : sevenSecondsList) {
            if (event.getPrice() < minPrice) {
                minPrice = event.getPrice();
            }
        }
        if (minPrice > triggerPrice) {
            triggerPrice = minPrice - 1;
            publisher.publish(new TriggerPriceChangedEvent(triggerPrice));
        }
    }

    public void handleSendToSelfIn(Event event, long timestamp) {
        if (event instanceof RemoveFromSevenSecondsQueueCommand) {
            this.handleRemoveFromSevenSecondQueue(event);
        } else if (event instanceof  RemoveFromElevenSecondsQueueCommand) {
            this.handleRemoveFromElevenSecondQueue(event);
        }
    }

    public void handlePositionAcquired(PositionAcquired positionAcquiredEvent) {
        // does nothing
    }

    public long getTriggerPrice() {
        return triggerPrice;
    }
}
