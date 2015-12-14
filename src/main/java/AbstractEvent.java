import java.math.BigDecimal;

/**
 * Created by jan on 14/12/15.
 */
public class AbstractEvent implements Event {

    private final long timestamp;
    private final long price;

    public AbstractEvent(long timestamp, long price) {
        this.timestamp = timestamp;
        this.price = price;
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public long getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "Event [" +getTimestamp() + ", " + getPrice() + "]";
    }
}
