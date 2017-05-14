package org.jsoftware.utils.retriable;

import java.util.concurrent.ThreadFactory;

/**
 * ThreadFactory for retriable
 * @author m-szalik
 */
class RetriableThreadFactory implements ThreadFactory {
    private int threadCount;

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(r, "retriable-thread-" + threadCount);
        t.setDaemon(true);
        threadCount++;
        return t;
    }
}
