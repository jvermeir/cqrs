package example1;

/**
 * Created by jan on 14/12/15.
 */
public class TriggerSellOccuredEvent extends AbstractEvent {
    public TriggerSellOccuredEvent() {
        super(0L, 0L);
    }

    public TriggerSellOccuredEvent(long timestamp, long price) {
        super(timestamp, price);
    }
}
