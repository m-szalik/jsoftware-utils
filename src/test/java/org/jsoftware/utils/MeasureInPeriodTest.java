package org.jsoftware.utils;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

/**
 * @author szalik
 */
public class MeasureInPeriodTest {
    private TestClock clock;
    private MeasureInPeriod measureInPeriod;

    @Before
    public void setUp() throws Exception {
        clock = new TestClock();
        measureInPeriod = new MeasureInPeriod(clock, TimeUnit.MINUTES.toMillis(1));
    }

    @Test
    public void testClean() throws Exception {
        assertEquals(0, measureInPeriod.get());
    }

    @Test
    public void testTwoHits() throws Exception {
        measureInPeriod.hit();
        measureInPeriod.hit();
        assertEquals(2, measureInPeriod.get());
    }

    @Test
    public void testExpire() throws Exception {
        measureInPeriod.hit();
        clock.update(clock.instant().plusSeconds(70));
        assertEquals(0, measureInPeriod.get());
    }

    @Test
    public void testFullWithCleanup() throws Exception {
        for(int i=0; i<25; i++) {
            measureInPeriod.hit();
        }
        clock.update(clock.instant().plusSeconds(30));
        assertEquals(25, measureInPeriod.get());
        measureInPeriod.hit();
        measureInPeriod.hit();
        clock.update(clock.instant().plusSeconds(40));
        for(int i=0; i<25; i++) {
            measureInPeriod.hit();
        }
        assertEquals(27, measureInPeriod.get());
    }

    @Test
    public void testWithDefaultClock() throws Exception {
        MeasureInPeriod measure = new MeasureInPeriod(TimeUnit.SECONDS.toMillis(20));
        measure.hit();
        measure.hit();
        assertEquals(2, measure.get());
    }

    @Test
    public void testClear() throws Exception {
        measureInPeriod.hit(3);
        assertEquals(3, measureInPeriod.get());
        measureInPeriod.clear();
        assertEquals(0, measureInPeriod.get());
    }
}