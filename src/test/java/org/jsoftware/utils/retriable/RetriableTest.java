package org.jsoftware.utils.retriable;

import org.junit.Assert;

import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 */
public class RetriableTest {

    @org.junit.Test
    public void testRetryLimit() throws Exception {
        final AtomicInteger counter = new AtomicInteger(0);
        Future<Void> f = Retriable.doTry(new Runnable() {
            @Override
            public void run() {
                counter.incrementAndGet();
                throw new MyErrorException();
            }
        }, 5, RetriableDelayFunction.constFunction(100));
        try {
            f.get();
            Assert.fail();
        } catch (ExecutionException ex) {
            Assert.assertEquals(5, counter.get());
            Assert.assertFalse(f.isDone());
            Assert.assertTrue(f.isCancelled());
        }
    }

    @org.junit.Test
    public void testRetrySuccessOn3rdRunnable() throws Exception {
        long t0 = System.currentTimeMillis();
        final AtomicInteger counter = new AtomicInteger(0);
        Future<Void> f = Retriable.doTry(new Runnable() {
            @Override
            public void run() {
                if (counter.incrementAndGet() != 3) {
                    throw new MyErrorException();
                }
            }
        }, 5, RetriableDelayFunction.constFunction(100));
        f.get();
        Assert.assertEquals(3, counter.get());
        Assert.assertTrue(200 < System.currentTimeMillis() - t0);
    }

    @org.junit.Test
    public void testRetrySuccessOn3rdWithCallable() throws Exception {
        long t0 = System.currentTimeMillis();
        final AtomicInteger counter = new AtomicInteger(0);
        Future<String> f = Retriable.doTry(new Callable<String>() {
            @Override
            public String call() throws Exception {
                if (counter.incrementAndGet() != 3) {
                    throw new MyErrorException();
                }
                return "OK";
            }
        }, 5, RetriableDelayFunction.constFunction(100));
        String result = f.get();
        Assert.assertEquals("OK", result);
        Assert.assertEquals(3, counter.get());
        Assert.assertTrue(200 < System.currentTimeMillis() - t0);
    }

    @org.junit.Test
    public void testRetryBreak() throws Exception {
        final AtomicInteger counter = new AtomicInteger(0);
        Future<Void> f = Retriable.doTry(new Runnable() {
            @Override
            public void run() {
                counter.incrementAndGet();
                throw new MyErrorException();
            }
        }, 5, new RetriableDelayFunction() {
            @Override
            public long retryWait(int tryNo) {
                return counter.get() == 3 ? -1 : 10;
            }
        });
        try {
            f.get();
            Assert.fail();
        } catch (ExecutionException e) {
            /* OK */
        }
        Assert.assertEquals(3, counter.get());
    }

    @org.junit.Test(expected = CancellationException.class)
    public void testCancel() throws Exception {
        Future<Void> f = Retriable.doTry(new Runnable() {
            @Override
            public void run() {
                throw new MyErrorException();
            }
        }, 5);
        Assert.assertFalse(f.isCancelled());
        Assert.assertTrue(f.cancel(true));
        Assert.assertTrue(f.isCancelled());
        f.get();
    }

    @org.junit.Test(expected = TimeoutException.class)
    public void testGetWithTimeout() throws Exception {
        Future<Void> f = Retriable.doTry(new Runnable() {
            @Override
            public void run() {
                throw new MyErrorException();
            }
        }, 5, RetriableDelayFunction.constFunction(1000));
        f.get(1, TimeUnit.SECONDS);
    }

    @org.junit.Test(expected = ExecutionException.class)
    public void testGetWithTimeoutFail() throws Exception {
        Future<Void> f = Retriable.doTry(new Runnable() {
            @Override
            public void run() {
                throw new MyErrorException();
            }
        }, 5, RetriableDelayFunction.constFunction(1000));
        f.get(1, TimeUnit.MINUTES);
    }

    @org.junit.Test
    public void testCancelDone() throws Exception {
        Future<Void> f = Retriable.doTry(new Runnable() {
            @Override
            public void run() {

            }
        }, 2, RetriableDelayFunction.constFunction(1000));
        f.get();
        Assert.assertFalse(f.cancel(true));
    }
}

class MyErrorException extends RuntimeException {
    public MyErrorException() {
        super("MyTestError");
    }
}