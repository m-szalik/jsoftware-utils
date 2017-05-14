package org.jsoftware.utils.time;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
        assertEquals(t1, t2);
    }

    @Test
    public void testUpdate() throws Exception {
        Instant instant = Instant.now().plus(12, ChronoUnit.MINUTES);
        long t1 = clock.millis();
        clock.update(instant);
        assertNotEquals(t1, clock.millis());
        assertEquals(instant.toEpochMilli(), clock.millis());
    }

    @Test
    public void testTimeZone() throws Exception {
        ZoneId zoneId = ZoneId.of("+4");
        TestClock utcPlus4TestClock = clock.withZone(zoneId);
        LocalDateTime local1 = LocalDateTime.now(clock);
        LocalDateTime local2 = LocalDateTime.now(utcPlus4TestClock);
        LocalDateTime local1Normalized = local1.plus(4, ChronoUnit.HOURS);
        assertEquals(local1Normalized, local2);
    }

    @Test
    public void testTimeZoneSameZone() throws Exception {
        ZoneId zoneId = clock.getZone();
        Clock clock2 = clock.withZone(zoneId);
        assertEquals(zoneId, clock2.getZone());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateNull() throws Exception {
        clock.update(null);
    }

    @Test
    public void testToString() throws Exception {
        String str = clock.toString();
        assertNotNull(str);
        assertTrue("toString returned blank value", str.trim().length() > 0);
    }

    @Test
    public void testPlus10Minutes() throws Exception {
        long t0 = clock.millis();
        clock.plus(10, TimeUnit.MINUTES);
        long t1 = clock.millis();
        Assert.assertEquals(TimeUnit.MINUTES.toMillis(10), t1 - t0);
    }

    @Test
    public void testPlus1000Millis() throws Exception {
        long t0 = clock.millis();
        clock.plus(1000L);
        long t1 = clock.millis();
        Assert.assertEquals(1000, t1 - t0);
    }

    @Test
    public void testMinus1Day() throws Exception {
        long t0 = clock.millis();
        int n0 = clock.instant().getNano();
        clock.plus(-1, ChronoUnit.DAYS);
        long t1 = clock.millis();
        int n1 = clock.instant().getNano();
        Assert.assertEquals(TimeUnit.DAYS.toMillis(-1), t1 - t0);
        Assert.assertEquals(n0, n1);
    }


    @Test
    public void testPlus10Nanos() throws Exception {
        long t0 = clock.millis();
        int n0 = clock.instant().getNano();
        clock.plus(10, TimeUnit.NANOSECONDS);
        long t1 = clock.millis();
        int n1 = clock.instant().getNano();
        Assert.assertEquals(t1, t0);
        Assert.assertEquals(10, n1 - n0);
    }
}