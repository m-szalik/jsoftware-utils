package org.jsoftware.utils;

import org.junit.Before;
import org.junit.Test;

import java.time.Clock;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

/**
 * @author szalik
 */
public class MeasureInPeriodTest {
    private MeasureInPeriod measureInPeriod;

    @Before
    public void setUp() throws Exception {
        measureInPeriod = new MeasureInPeriod(Clock.systemUTC(), TimeUnit.SECONDS.toMillis(1));
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
        Thread.sleep(TimeUnit.SECONDS.toMillis(2));
        assertEquals(0, measureInPeriod.get());
    }
}