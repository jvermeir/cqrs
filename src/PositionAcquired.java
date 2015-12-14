import java.math.BigDecimal;

/**
 * Created by jan on 14/12/15.
 */
public class PositionAcquired extends AbstractEvent {
    public  PositionAcquired(long timestamp, long price) {
        super(timestamp, price);
    }
}
