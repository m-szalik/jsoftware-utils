package org.jsoftware.utils.retriable;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Retry any task you want
 * @author m-szalik
 */
public class Retriable {
    private static final Retriable INSTANCE = new Retriable();
    private static final RetriableDelayFunction DEFAULT_FUNCT = RetriableDelayFunction.expFunction();
    private final ScheduledExecutorService executorService;

    private Retriable() {
        executorService = Executors.newScheduledThreadPool(1, new RetriableThreadFactory());
    }

    private <T> Future<T> doTryInternal(final Callable<T> callable, final int limit, final RetriableDelayFunction delayFunction) {
        final RetriableFuture<T> future = new RetriableFuture<>();
        executorService.execute(new RetriableRunnable<T>(callable, limit, delayFunction, future));
        return future;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        executorService.shutdownNow();
    }

    /**
     * Retry task
     * @param callable task to retry
     * @param limit retry limit
     * @param delayFunction time delay between retries
     * @param <T> result type
     * @return Future for this task
     */
    public static <T> Future<T> doTry(final Callable<T> callable, final int limit, final RetriableDelayFunction delayFunction) {
        return INSTANCE.doTryInternal(callable, limit, delayFunction);
    }

    /**
     * Retry task with default delay between retries
     * @param callable task to retry
     * @param limit retry limit
     * @param <T> result type
     * @return Future for this task
     */
    public static <T> Future<T> doTry(final Callable<T> callable, final int limit) {
        return INSTANCE.doTryInternal(callable, limit, DEFAULT_FUNCT);
    }

    /**
     * Retry task
     * @param runnable task to retry
     * @param limit retry limit
     * @param delayFunction time delay between retries
     * @return Future for this task
     */
    public static Future<Void> doTry(final Runnable runnable, final int limit, final RetriableDelayFunction delayFunction) {
        return INSTANCE.doTryInternal(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                runnable.run();
                return null;
            }
        }, limit, delayFunction);
    }

    /**
     * Retry task with default delay between retries
     * @param runnable task to retry
     * @param limit retry limit
     * @return Future for this task
     */
    public static Future<Void> doTry(final Runnable runnable, final int limit) {
        return INSTANCE.doTryInternal(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                runnable.run();
                return null;
            }
        }, limit, DEFAULT_FUNCT);
    }


    class RetriableRunnable<T> implements Runnable {
        private final Callable<T> callable;
        private final int limit;
        private final RetriableDelayFunction delayFunction;
        private final RetriableFuture<T> future;

        RetriableRunnable(Callable<T> callable, int limit, RetriableDelayFunction delayFunction, RetriableFuture<T> future) {
            this.callable = callable;
            this.limit = limit;
            this.delayFunction = delayFunction;
            this.future = future;
        }

        @Override
        public void run() {
            if (! future.isCancelled()) {
                int tryNo = future.tryCount() + 1;
                try {
                    T result = callable.call();
                    future.done(result);
                } catch (Exception ex) {
                    future.setLastError(ex);
                    if (tryNo < limit) {
                        long delay = delayFunction.retryWait(tryNo);
                        if (delay >= 0) {
                            executorService.schedule(this, delay, TimeUnit.MILLISECONDS);
                        } else {
                            future.finish();
                        }
                    } else {
                        future.finish();
                    }
                } finally {
                    future.setTryCount(tryNo);
                }
            } else {
                future.finish();
            }
        }
    }
}

