package org.jsoftware.utils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 */
public class SimpleCacheTest {
    private Instant now;
    private SimpleCache<Object,Object> cache;

    @Before
    public void setUp() throws Exception {
        now = Instant.now();
        cache = new SimpleCache<Object, Object>(1000, 3) {
            @Override
            protected Instant now() {
                return now;
            }
        };
    }

    @Test
    public void testFetch() throws Exception {
        Object rv1 = cache.fetch("x", () -> new Object());
        Object rv2 = cache.fetch("x", () -> new Object());
        Assert.assertEquals(rv1, rv2);
        now = now.plusSeconds(2);
        Object rv3 = cache.fetch("x", () -> new Object());
        Assert.assertNotEquals(rv1, rv3);
        Assert.assertNotEquals(rv2, rv3);
    }

    @Test
    public void testPutGetGet() throws Exception {
        Object o1 = new Object();
        cache.put("x", o1);
        Object g1 = cache.get("x");
        Assert.assertEquals(o1, g1);
        now = now.plusSeconds(2);
        Object g2 = cache.get("x");
        Assert.assertNull(g2);
    }

    @Test
    public void testSize() throws Exception {
        Assert.assertTrue(cache.isEmpty());
        cache.put("x1", "a");
        Assert.assertFalse(cache.isEmpty());
        Assert.assertEquals(1, cache.size());
        cache.put("x2", "a");
        cache.clear();
        Assert.assertTrue(cache.isEmpty());
    }

    @Test
    public void testConcurrentAccessTheSameKey() throws Exception {
        final AtomicBoolean state = new AtomicBoolean(false);
        Thread t = new Thread(()->{
            cache.fetch("x", ()-> {
                state.set(true);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                return Thread.currentThread().getName();
            });
        });

        t.start();
        while(! state.get()) {
            Thread.sleep(5);
        }
        Object r = cache.fetch("x", () -> Thread.currentThread().getName());
        Assert.assertEquals(t.getName(), r);
    }

    @Test
    public void testConcurrentAccessDifferentKeys() throws Exception {
        final AtomicBoolean state = new AtomicBoolean(false);
        final StringBuilder tOut = new StringBuilder();
        Thread t = new Thread(()->{
            String str = (String) cache.fetch("a", ()-> {
                state.set(true);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                return Thread.currentThread().getName();
            });
            tOut.append(str);
        });

        t.start();
        while(! state.get()) {
            Thread.sleep(5);
        }
        long ts = System.currentTimeMillis();
        Object r = cache.fetch("b", () -> Thread.currentThread().getName());
        ts = System.currentTimeMillis() - ts;
        t.join();
        Assert.assertEquals(Thread.currentThread().getName(), r);
        Assert.assertEquals(t.getName(), tOut.toString());
        Assert.assertTrue("Too long", ts < 500); // less then 500ms
    }
}