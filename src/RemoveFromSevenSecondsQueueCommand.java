/**
 * Created by jan on 14/12/15.
 */
public class RemoveFromSevenSecondsQueueCommand extends AbstractEvent {
    public RemoveFromSevenSecondsQueueCommand(long timestamp, long price) {
        super(timestamp, price);
    }

    Event theEvent;
    public RemoveFromSevenSecondsQueueCommand(Event event) {
        // This is not fine, but it'll do
        super (0L, 0L);
        theEvent = event;
    }

    public Event getTheEvent() {
        return theEvent;
    }
}
