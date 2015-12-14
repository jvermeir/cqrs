package example1;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

/**
 * Created by jan on 14/12/15.
 */
public class ProcessManagerTest {

    Publisher publisher = new Publisher();
    ProcessManager processManager = new ProcessManager(publisher);

    @Before
    public void setup() {
        publisher.clear();
        processManager.handlePositionAcquired(new PositionAcquired(1L, 1L));
    }

    @Test
    public void testPositionAcquired() {
        Assert.assertEquals(0, processManager.elevenSecondsList.size());
        Assert.assertEquals(0, processManager.sevenSecondsList.size());
    }

    @Test
    public void testPriceIncreased() {
        processManager.handlePriceTickOccured(new PriceTickOccured(2L, 2L));
        Assert.assertEquals(1, processManager.sevenSecondsList.size());
        Assert.assertEquals(1, processManager.elevenSecondsList.size());
    }

    @Test
    public void testTriggerPriceIncreased() {
        // given
        PriceTickOccured priceTick = new PriceTickOccured(2L, 200L);
        processManager.handlePriceTickOccured(priceTick);
        processManager.handlePriceTickOccured(new PriceTickOccured(3L, 210L));
        // when
        publisher.clear();
        processManager.handleRemoveFromSevenSecondQueue(priceTick);
        // then
        Event theEvent= null;
        for (Event event : publisher.getEvents()) {
            if (event instanceof TriggerPriceChangedEvent) {
                theEvent = event;
                break;
            }
        }
        Assert.assertEquals(209L, theEvent.getPrice());
    }

    @Test
    public void testSellOccurs() {
        // given
        PriceTickOccured priceTick = new PriceTickOccured(2L, 200L);
        processManager.handlePriceTickOccured(priceTick);
        processManager.handlePriceTickOccured(new PriceTickOccured(3L, 10L));
        // when
        publisher.clear();
        processManager.handleRemoveFromElevenSecondQueue(priceTick);
        // then
        assertEquals(1, countTriggerSellOccuredEvents());
    }

    private int countTriggerSellOccuredEvents() {
        int count=0;
        for (Event event : publisher.getEvents()) {
            if (event instanceof TriggerSellOccuredEvent) {
                count++;
            }
        }
        return count;
    }

    @Test
    public void testSellOccursOnlyOnce() {
        // given
        PriceTickOccured priceTick = new PriceTickOccured(2L, 200L);
        processManager.handlePriceTickOccured(priceTick);
        PriceTickOccured secondPriceTick = new PriceTickOccured(3L, 10L);
        processManager.handlePriceTickOccured(secondPriceTick);
        // when
        publisher.clear();
        processManager.handleRemoveFromElevenSecondQueue(priceTick);
        processManager.handleRemoveFromElevenSecondQueue(secondPriceTick);
        assertEquals(1, countTriggerSellOccuredEvents());
    }

}