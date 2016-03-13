package org.jsoftware.utils;

import java.time.Clock;
import java.util.concurrent.TimeUnit;

/**
 * Allows to measure time (millis) of an action.
 * @author szalik
 */
public final class TimeWatch {
    private final Clock clock;
    private long t1, t2 = Long.MIN_VALUE;

    /**
     * TimeWatch with Clock.systemDefaultZone() clock
     * @see Clock#systemDefaultZone()
     */
    public TimeWatch() {
        this(Clock.systemDefaultZone());
    }

    TimeWatch(Clock clock) {
        this.clock = clock;
        this.t1 = clock.millis();
    }

    /**
     * Reset. Start it again.
     */
    public void reset() {
        t1 = clock.millis();
        t2 = Long.MIN_VALUE;
    }

    /**
     * Stop measurements.
     */
    public void stop() {
        t2 = clock.millis();
    }

    /**
     * @return period in ms
     */
    public long getDuration() {
        return (t2 > 0 ? t2 : clock.millis()) - t1;
    }

    /**
     * @return period in human friendly format.
     */
    public String getDurationHuman() {
        long duration = getDuration();
        if (duration > 0) {
            if (duration > TimeUnit.MINUTES.toMillis(1)) { // more then 1 minute
                int ms = (int) (duration % 1000);
                int seconds = (int) (duration / 1000) % 60;
                int minutes = (int) ((duration / (1000*60)) % 60);
                int hours   = (int) ((duration / (1000*60*60)) % 24);
                long days = TimeUnit.MILLISECONDS.toDays(duration);
                StringBuilder sb = new StringBuilder();
                if (days > 0) {
                    sb.append(days).append('d');
                }
                lz(sb, hours).append(':');
                lz(sb, minutes).append(':');
                lz(sb, seconds).append('.').append(ms);
                return sb.toString();
            } else {
                return duration + "ms.";
            }
        } else {
            return "none";
        }
    }

    private static StringBuilder lz(StringBuilder sb, int val) {
        if (val < 10) {
            sb.append('0');
        }
        sb.append(val);
        return sb;
    }

    @Override
    public String toString() {
        return getDurationHuman();
    }


}
