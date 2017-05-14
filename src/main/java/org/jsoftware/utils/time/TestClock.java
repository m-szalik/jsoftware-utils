package org.jsoftware.utils.time;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

/**
 * Clock useful for testing. Returned time is modified only after #update(Instant) method is called.
 * @author m-szalik
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
    public TestClock update(Instant instant) {
        if (instant == null) {
            throw new IllegalArgumentException("Instant cannot be null.");
        }
        this.instant = instant;
        return this;
    }

    public TestClock plus(long amount, ChronoUnit unit) {
        Instant instant = this.instant.plus(amount, unit);
        return update(instant);
    }

    public TestClock plus(int amount, TimeUnit timeUnit) {
        if (timeUnit == TimeUnit.NANOSECONDS) {
            plus(0L, amount);
        } else {
            plus(timeUnit.toMillis(amount), 0L);
        }
        return this;
    }

    /** Update clock by adding millis
     * @param millis milliseconds to add
     */
    public TestClock plus(long millis) {
        return plus(millis, 0L);
    }

    private TestClock plus(long millis, long nanos) {
        Instant instant = this.instant.plusMillis(millis).plusNanos(nanos);
        return update(instant);
    }

    @Override
    public String toString() {
        return instant.toString();
    }
}
