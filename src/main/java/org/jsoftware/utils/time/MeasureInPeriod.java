package org.jsoftware.utils.time;

import java.time.Clock;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Measure hits per period.
 * @author m-szalik
 */
public class MeasureInPeriod {
    private static final int CLEANUP_WHEN = 10; // cleanup once per X hits operations
    private final ReadWriteLock rwLock;
    private final Clock clock;
    private final long periodMillis;
    private int cleanupCounter;
    private final LinkedList<Long> buffer;

    /**
     * Create measurement
     * @param clock clock
     * @param periodMillis period for measurement
     */
    public MeasureInPeriod(Clock clock, long periodMillis) {
        this.clock = clock;
        this.periodMillis = periodMillis;
        this.buffer = new LinkedList<>();
        this.rwLock = new ReentrantReadWriteLock();
    }

    /**
     * Create measurement using default system clock
     * @param periodMillis period for measurement
     */
    public MeasureInPeriod(long periodMillis) {
        this(Clock.systemDefaultZone(), periodMillis);
    }

    /**
     * Report a hit
     */
    public void hit() {
        long t = clock.millis();
        writeLock(() -> {
            buffer.add(t);
            cleanupCounter++;
            cleanup();
        });
    }

    /**
     * Multiple hits
     * @param hits number of hits
     */
    public void hit(int hits) {
        long t = clock.millis();
        writeLock(() -> {
            for(int i=0; i<hits; i++) {
                buffer.add(t);
            }
            cleanupCounter++;
            cleanup();
        });
    }

    /**
     * @return current hit rate
     */
    public int get() {
        long validTime = clock.millis() - periodMillis;
        int c = 0;
        Lock lock = rwLock.readLock();
        try {
            lock.lock();
            for (Long t : buffer) {
                if (t > validTime) {
                    c++;
                }
            }
            return c;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Reset measurement
     */
    public void reset() {
        writeLock(buffer::clear);
    }

    /**
     * @see #reset()
     */
    public void clear() {
        reset();
    }

    private void writeLock(Runnable action) {
        Lock lock = rwLock.writeLock();
        try {
            lock.lock();
            action.run();
        } finally {
            lock.unlock();
        }
    }

    private void cleanup() {
        if (cleanupCounter > CLEANUP_WHEN) { // perform cleanup of buffer
            Iterator<Long> it = buffer.iterator();
            long validTime = clock.millis() - periodMillis;
            while(it.hasNext()) {
                long t = it.next();
                if (t <= validTime) {
                    it.remove();
                }
            }
        }
    }


}
