package org.jsoftware.utils.retriable;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Future of retriable task
 * @author m-szalik
 */
public class RetriableFuture<V> implements Future<V> {
    private final Object lock = new Object();
    private volatile int tryCount;
    private volatile Exception lastError;
    private volatile boolean canceled, done, finished;
    private volatile V result;

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        if (! done) {
            canceled = true;
            return true;
        }
        return false;
    }

    @Override
    public boolean isCancelled() {
        return canceled || (finished && ! done);
    }

    @Override
    public boolean isDone() {
        return done;
    }

    private V getResult() throws ExecutionException {
        if (! finished) {
            throw new IllegalStateException();
        }
        if (done) {
            return result;
        }
        if (canceled) {
            throw new CancellationException();
        }
        throw new ExecutionException(lastError);
    }

    @Override
    public V get() throws InterruptedException, ExecutionException {
        if (! finished) {
            synchronized (lock) {
                if (! finished) {
                    lock.wait();
                }
            }
        }
        return getResult();
    }

    @Override
    public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        if (! finished) {
            synchronized (lock) {
                if (! finished) {
                    lock.wait(unit.toMillis(timeout));
                    if (! finished) {
                        throw new TimeoutException();
                    }
                }
            }
        }
        return getResult();
    }
    
    public int tryCount() {
        return tryCount;
    }

    public Exception getLastError() {
        return lastError;
    }

    void setTryCount(int tryCount) {
        this.tryCount = tryCount;
    }

    void setLastError(Exception lastError) {
        this.lastError = lastError;
    }
    
    synchronized void done(V result) {
        this.done = true;
        this.result = result;
        finish();
    }

    void finish() {
        finished = true;
        synchronized (lock) {
            lock.notifyAll();
        }
    }
}
