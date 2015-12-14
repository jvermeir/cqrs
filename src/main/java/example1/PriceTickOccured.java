package example1;

/**
 * Created by jan on 14/12/15.
 */
public class PriceTickOccured extends AbstractEvent {
    public PriceTickOccured (long timestamp, long price) {
        super(timestamp, price);
    }
}
