package example1;

/**
 * Created by jan on 14/12/15.
 */
public class SendToSelfEvent extends AbstractEvent {

    public SendToSelfEvent(long timestamp, long price) {
        super(timestamp, price);
    }
}
