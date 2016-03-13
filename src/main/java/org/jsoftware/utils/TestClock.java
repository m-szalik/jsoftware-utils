package org.jsoftware.utils;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

/**
 * Clock useful for testing. Returned time is modified only after #update(Instant) method is called.
 * @author szalik
 */
public class TestClock extends Clock {
    private final ZoneId zoneId;
    private volatile Instant instant;

    public TestClock() {
        this(ZoneId.systemDefault());
    }

    public TestClock(ZoneId zoneId) {
        this(zoneId, Instant.now());
    }

    private TestClock(ZoneId zoneId, Instant instant) {
        this.zoneId = zoneId;
        this.instant = instant;
    }

    @Override
    public ZoneId getZone() {
        return zoneId;
    }

    @Override
    public TestClock withZone(ZoneId zone) {
        if (zoneId.equals(zone)) {
            return this;
        } else {
            return new TestClock(zone, instant);
        }
    }

    @Override
    public Instant instant() {
        return instant;
    }

    /**
     * Update clock time.
     * @param instant new clock time
     */
    public void update(Instant instant) {
        this.instant = instant;
    }

    @Override
    public String toString() {
        return instant.toString();
    }
}
