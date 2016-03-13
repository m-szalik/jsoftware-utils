package org.jsoftware.utils;


import org.junit.Assert;
import org.junit.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class TimeWatchTest {

    @Test
    public void testMeasure() throws InterruptedException {
        TimeWatch timeWatch = new TimeWatch();
        Thread.sleep(1200);
        String ts = timeWatch.getDurationHuman();
        org.junit.Assert.assertTrue(ts.matches("\\d{4}ms\\."));
    }

    @Test
    public void testDuration() throws Exception {
        TestClock clock = new TestClock();
        TimeWatch timeWatch = new TimeWatch(clock);
        Instant instant = clock.instant().plus(12, ChronoUnit.MINUTES).plus(9, ChronoUnit.SECONDS);
        long diff = instant.toEpochMilli() - clock.millis();
        clock.update(instant);
        timeWatch.stop();
        clock.update(instant.plus(7, ChronoUnit.DAYS));
        long duration = timeWatch.getDuration();
        Assert.assertEquals(diff, duration);
    }

    @Test
    public void testDurationHuman() throws Exception {
        TestClock clock = new TestClock();
        TimeWatch timeWatch = new TimeWatch(clock);
        Instant instant = clock.instant().plus(241, ChronoUnit.HOURS).plus(12, ChronoUnit.MINUTES).plus(9, ChronoUnit.SECONDS).plus(166, ChronoUnit.MILLIS);
        clock.update(instant);
        String duration = timeWatch.getDurationHuman();
        Assert.assertEquals("10d01:12:09.166", duration);
    }
}
