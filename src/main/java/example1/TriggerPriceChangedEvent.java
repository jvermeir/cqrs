package example1;

/**
 * Created by jan on 14/12/15.
 */
public class TriggerPriceChangedEvent extends AbstractEvent {
    public TriggerPriceChangedEvent(long triggerPrice) {
        super(0L, triggerPrice);
    }

    public TriggerPriceChangedEvent(long timestamp, long price) {
        super(timestamp, price);
    }
}
