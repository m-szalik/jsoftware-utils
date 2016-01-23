package org.jsoftware.utils;

/**
 * Allows to measure time (millis) of an action.
 * @author szalik
 */
public final class TimeWatch {
    private long t1, t2 = Long.MIN_VALUE;

    public TimeWatch() {
        t1 = System.currentTimeMillis();
    }

    public void reset() {
        t1 = System.currentTimeMillis();
        t2 = Long.MIN_VALUE;
    }

    public void stop() {
        t2 = System.currentTimeMillis();
    }

    public long getDuration() {
        return (t2 > 0 ? t2 : System.currentTimeMillis()) - t1;
    }

    public String getDurationHuman() {
        long duration = getDuration();
        if (duration > 0) {
            if (duration > 60000) { // more then 1 minute
                int ms = (int) (duration % 1000);
                int seconds = (int) (duration / 1000) % 60;
                int minutes = (int) ((duration / (1000*60)) % 60);
                int hours   = (int) ((duration / (1000*60*60)) % 24);
                StringBuilder sb = new StringBuilder();
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


    public static TimeWatch newInstance() {
        return new TimeWatch();
    }
}
