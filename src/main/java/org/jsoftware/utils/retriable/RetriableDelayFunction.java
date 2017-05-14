package org.jsoftware.utils.retriable;

import java.util.concurrent.TimeUnit;

/**
 * Delay between retries function.
 * <p>
 * How long to wait for next try.
 * </p>
 * @author m-szalik
 */
public abstract class RetriableDelayFunction {
    private static final RetriableDelayFunction DEFAULT_LINEAR = linearFunction(10000);
    private static final RetriableDelayFunction NO_DEALY = linearFunction(0);

    /**
     * @param tryNo try count
     * @return how long to wait in ms. If less then zero - break.
     */
    public abstract long retryWait(int tryNo);


    /**
     * Constant delay function
     * @param delayMillis delay in millis
     */
    public static RetriableDelayFunction constFunction(final long delayMillis) {
        return new RetriableDelayFunction() {
            @Override
            public long retryWait(int tryNo) {
                return delayMillis;
            }
        };
    }

    /**
     * Exponential delay function
     */
    public static RetriableDelayFunction expFunction() {
        return new RetriableDelayFunction() {
            @Override
            public long retryWait(int tryNo) {
                long d = (long) (1000 + Math.pow(10, Math.max(3, tryNo)));
                return Math.min(TimeUnit.DAYS.toMillis(7), d);
            }
        };
    }

    /**
     * Linear delay function (factor * tryNo)
     * @param factor a factor
     */
    public static RetriableDelayFunction linearFunction(final int factor) {
        return new RetriableDelayFunction() {
            @Override
            public long retryWait(int tryNo) {
                return tryNo * factor;
            }
        };
    }

    /**
     * Linear delay function with factor 10s.
     * @see #linearFunction(int)
     */
    public static RetriableDelayFunction linearFunction() {
        return DEFAULT_LINEAR;
    }

    /**
     * No delay function
     */
    public static RetriableDelayFunction noDealyFunction() {
        return NO_DEALY;
    }
}