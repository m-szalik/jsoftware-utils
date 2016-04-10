package org.jsoftware.utils;

import org.junit.Before;
import org.junit.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

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

}