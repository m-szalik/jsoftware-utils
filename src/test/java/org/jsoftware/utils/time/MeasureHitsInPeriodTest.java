package org.jsoftware.utils.time;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

/**
 * @author m-szalik
 */
public class MeasureHitsInPeriodTest {
    private TestClock clock;
    private MeasureHitsInPeriod measureHitsInPeriod;

    @Before
    public void setUp() throws Exception {
        clock = new TestClock();
        measureHitsInPeriod = new MeasureHitsInPeriod(clock, TimeUnit.MINUTES.toMillis(1));
    }

    @Test
    public void testClean() throws Exception {
        assertEquals(0, measureHitsInPeriod.get());
    }

    @Test
    public void testTwoHits() throws Exception {
        measureHitsInPeriod.hit();
        measureHitsInPeriod.hit();
        assertEquals(2, measureHitsInPeriod.get());
    }

    @Test
    public void testExpire() throws Exception {
        measureHitsInPeriod.hit();
        clock.update(clock.instant().plusSeconds(70));
        assertEquals(0, measureHitsInPeriod.get());
    }

    @Test
    public void testFullWithCleanup() throws Exception {
        for(int i=0; i<25; i++) {
            measureHitsInPeriod.hit();
        }
        clock.update(clock.instant().plusSeconds(30));
        assertEquals(25, measureHitsInPeriod.get());
        measureHitsInPeriod.hit();
        measureHitsInPeriod.hit();
        clock.update(clock.instant().plusSeconds(40));
        for(int i=0; i<25; i++) {
            measureHitsInPeriod.hit();
        }
        assertEquals(27, measureHitsInPeriod.get());
    }

    @Test
    public void testWithDefaultClock() throws Exception {
        MeasureHitsInPeriod measure = new MeasureHitsInPeriod(TimeUnit.SECONDS.toMillis(20));
        measure.hit();
        measure.hit();
        assertEquals(2, measure.get());
    }

    @Test
    public void testClear() throws Exception {
        measureHitsInPeriod.hit(3);
        assertEquals(3, measureHitsInPeriod.get());
        measureHitsInPeriod.clear();
        assertEquals(0, measureHitsInPeriod.get());
    }
}