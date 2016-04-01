package org.jsoftware.utils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

public class TestClockTest {
    private TestClock clock;

    @Before
    public void setUp() throws Exception {
        clock = new TestClock(ZoneId.of("UTC"));
    }

    @Test
    public void testNoChange() throws Exception {
        long t1 = clock.millis();
        Thread.sleep(2000);
        long t2 = clock.millis();
        Assert.assertEquals(t1, t2);
    }

    @Test
    public void testUpdate() throws Exception {
        Instant instant = Instant.now().plus(12, ChronoUnit.MINUTES);
        long t1 = clock.millis();
        clock.update(instant);
        Assert.assertNotEquals(t1, clock.millis());
        Assert.assertEquals(instant.toEpochMilli(), clock.millis());
    }

    @Test
    public void testTimeZone() throws Exception {
        ZoneId zoneId = ZoneId.of("+4");
        TestClock utcPlus4TestClock = clock.withZone(zoneId);
        LocalDateTime local1 = LocalDateTime.now(clock);
        LocalDateTime local2 = LocalDateTime.now(utcPlus4TestClock);
        long hrsDiff = local2.getHour() - local1.getHour();
        Assert.assertSame(zoneId, utcPlus4TestClock.getZone());
        Assert.assertEquals(4, hrsDiff);
    }

    @Test
    public void testTimeZoneSameZone() throws Exception {
        ZoneId zoneId = clock.getZone();
        Clock clock2 = clock.withZone(zoneId);
        Assert.assertEquals(zoneId, clock2.getZone());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateNull() throws Exception {
        clock.update(null);
    }

}