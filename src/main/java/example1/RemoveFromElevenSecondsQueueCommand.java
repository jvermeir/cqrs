package example1;

/**
 * Created by jan on 14/12/15.
 */
public class RemoveFromElevenSecondsQueueCommand extends AbstractEvent {
    public RemoveFromElevenSecondsQueueCommand(long timestamp, long price) {
        super(timestamp, price);
    }
}
