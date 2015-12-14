package example1;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jan on 14/12/15.
 */
public class Publisher {

    List<Event> events = new ArrayList<Event>();

    public void clear() {
        events = new ArrayList<Event>();
    }

    public List<Event> getEvents() {
        return events;
    }

    public void publish(Event event) {
        events.add(event);
        System.out.println("example1.Publisher got event: " + event.getClass().getSimpleName() + " " + event);
    }
}
