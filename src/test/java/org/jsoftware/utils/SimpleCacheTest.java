package org.jsoftware.utils;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

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
    public void testFetchNotExisting() throws Exception {
        final Object rVal = new Object();
        Object r0 = cache.fetch("key", () -> rVal);
        Object r1 = cache.fetch("key", () -> new Object());
        Assert.assertEquals(rVal, r0);
        Assert.assertEquals(rVal, r1);
    }

    @Test
    public void testRemoveKey() throws Exception {
        cache.put(1, "one");
        cache.put(2, "two");
        Assert.assertEquals(2, cache.size());
        cache.remove(2);
        Assert.assertEquals(1, cache.size());
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

    @Test
    public void testPutAllAndContains() throws Exception {
        Map<Object,Object> map = new HashMap<>();
        map.put(0, "zero");
        map.put(1, "one");
        map.put(2, "two");
        cache.putAll(map);
        Assert.assertTrue(cache.containsValue("one"));
        Assert.assertTrue(cache.containsKey(0));
        Assert.assertFalse(cache.containsKey(10));
        Assert.assertFalse(cache.containsValue("xyz"));
    }

    @Test
    public void testRemoveEntriesOverSize() throws Exception {
        for(int i=0; i<5; i++) {
            cache.put(i, Integer.toBinaryString(i));
        }
        Set<Object> keys = cache.keySet();
        Assert.assertEquals(3, keys.size());
        Assert.assertThat(keys, CoreMatchers.hasItem(4));
        Assert.assertThat(keys, CoreMatchers.hasItem(3));
        Assert.assertThat(keys, CoreMatchers.hasItem(2));
    }
}