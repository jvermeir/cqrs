package restaurant;

/**
 * Created by jan on 15/12/15.
 */
public interface Startable {
    public void start();
    public void stop();
    public int getQueueCount();
    public String getName();
}

