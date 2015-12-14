import java.math.BigDecimal;

/**
 * Created by jan on 14/12/15.
 */
public class Sell extends AbstractEvent {

    public Sell(long timestamp, long price) {
        super(timestamp, price);
    }
}
