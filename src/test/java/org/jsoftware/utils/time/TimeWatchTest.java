package org.jsoftware.utils.time;


import org.junit.Before;
import org.junit.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TimeWatchTest {
    private TestClock clock;
    private TimeWatch timeWatch;

    @Before
    public void setUp() throws Exception {
        clock = new TestClock();
        timeWatch = new TimeWatch(clock);
    }

    @Test
    public void testMeasure() throws InterruptedException {
        clock.update(clock.instant().plus(1200, ChronoUnit.MILLIS));
        String ts = timeWatch.getDurationHuman();
        assertTrue(ts.matches("\\d{4}ms\\."));
    }

    @Test
    public void testDuration() throws Exception {
        Instant instant = clock.instant().plus(12, ChronoUnit.MINUTES).plus(9, ChronoUnit.SECONDS);
        long diff = instant.toEpochMilli() - clock.millis();
        clock.update(instant);
        timeWatch.stop();
        clock.update(instant.plus(7, ChronoUnit.DAYS));
        long duration = timeWatch.getDuration();
        assertEquals(diff, duration);
    }

    @Test
    public void testDurationHuman() throws Exception {
        Instant instant = clock.instant().plus(241, ChronoUnit.HOURS).plus(12, ChronoUnit.MINUTES).plus(9, ChronoUnit.SECONDS).plus(166, ChronoUnit.MILLIS);
        clock.update(instant);
        String duration = timeWatch.getDurationHuman();
        assertEquals("10d01:12:09.166", duration);
    }

    @Test
    public void testReset() throws Exception {
        clock.update(clock.instant().plus(9, ChronoUnit.SECONDS));
        assertEquals(TimeUnit.SECONDS.toMillis(9), timeWatch.getDuration());
        timeWatch.reset();
        clock.update(clock.instant().plus(2, ChronoUnit.SECONDS));
        assertEquals(TimeUnit.SECONDS.toMillis(2), timeWatch.getDuration());
    }

    @Test
    public void testRealTest() throws Exception {
        long t = System.currentTimeMillis();
        TimeWatch timeWatch = new TimeWatch();
        Thread.sleep(600);
        long ts = timeWatch.getDuration();
        assertTrue(ts >= 600);
        assertTrue(ts <= System.currentTimeMillis() - t);
    }

    @Test
    public void testToString() throws Exception {
        TimeWatch tw = new TimeWatch();
        Thread.sleep(1010);
        String str = tw.toString();
        assertTrue(str.matches("\\d{4}ms.+"));
    }
}
