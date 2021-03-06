package org.jsoftware.utils.cache;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class SimpleCacheTest {
    private Instant now;
    private SimpleCache<Object,Object> cache;

    @Before
    public void setUp() throws Exception {
        now = Instant.now();
        cache = new SimpleCache<Object, Object>(TimeUnit.SECONDS.toMillis(1), 3) {
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
        assertEquals(rVal, r0);
        assertEquals(rVal, r1);
    }

    @Test
    public void testRemoveKey() throws Exception {
        cache.put(1, "one");
        cache.put(2, "two");
        assertEquals(2, cache.size());
        cache.remove(2);
        assertEquals(1, cache.size());
    }

    @Test
    public void testFetch() throws Exception {
        Object rv1 = cache.fetch("x", () -> new Object());
        Object rv2 = cache.fetch("x", () -> new Object());
        assertEquals(rv1, rv2);
        now = now.plusSeconds(2);
        Object rv3 = cache.fetch("x", () -> new Object());
        assertNotEquals(rv1, rv3);
        assertNotEquals(rv2, rv3);
    }

    @Test
    public void testPutGetGet() throws Exception {
        Object o1 = new Object();
        cache.put("x", o1);
        Object g1 = cache.get("x");
        assertEquals(o1, g1);
        now = now.plusSeconds(2);
        Object g2 = cache.get("x");
        assertNull(g2);
    }

    @Test
    public void testSize() throws Exception {
        assertTrue(cache.isEmpty());
        cache.put("x1", "a");
        assertFalse(cache.isEmpty());
        assertEquals(1, cache.size());
        cache.put("x2", "a");
        cache.clear();
        assertTrue(cache.isEmpty());
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
                    throw new IllegalStateException(e);
                }
                return Thread.currentThread().getName();
            });
        });

        t.start();
        while(! state.get()) {
            Thread.sleep(5);
        }
        Object r = cache.fetch("x", () -> Thread.currentThread().getName());
        assertEquals(t.getName(), r);
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
                    throw new IllegalStateException(e);
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
        assertEquals(Thread.currentThread().getName(), r);
        assertEquals(t.getName(), tOut.toString());
        assertTrue("Too long", ts < 500); // less then 500ms
    }

    @Test
    public void testPutAllAndContains() throws Exception {
        Map<Object,Object> map = new HashMap<>();
        map.put(0, "zero");
        map.put(1, "one");
        map.put(2, "two");
        cache.putAll(map);
        assertTrue(cache.containsValue("one"));
        assertTrue(cache.containsKey(0));
        assertFalse(cache.containsKey(10));
        assertFalse(cache.containsValue("xyz"));
    }

    @Test
    public void testRemoveEntriesOverSize() throws Exception {
        for(int i=0; i<5; i++) {
            cache.put(i, Integer.toBinaryString(i));
        }
        Set<Object> keys = cache.keySet();
        assertEquals(3, keys.size());
        Assert.assertThat(keys, CoreMatchers.hasItem(4));
        Assert.assertThat(keys, CoreMatchers.hasItem(3));
        Assert.assertThat(keys, CoreMatchers.hasItem(2));
    }

    @Test
    public void testExpire() throws Exception {
        Assert.assertNull(cache.get("key"));
        cache.put("key", 1);
        Assert.assertEquals(1, cache.get("key"));
        now = now.plus(3, ChronoUnit.MINUTES);
        Assert.assertNull(cache.get("key"));
    }

    @Test
    public void testValuesKeysAndEntrySet() throws Exception {
        LinkedHashMap<String,Integer> map = new LinkedHashMap<>();
        map.put("key1", 1);
        map.put("key2", 2);
        map.put("key3", 3);
        cache.put("key1", 1);
        now = now.plus(500, ChronoUnit.MILLIS);
        cache.put("key2", 2);
        cache.put("key3", 3);
        Assert.assertArrayEquals(map.values().toArray(), cache.values().toArray());
        Assert.assertArrayEquals(map.keySet().toArray(), cache.keySet().toArray());
        Assert.assertArrayEquals(map.entrySet().toArray(), cache.entrySet().toArray());

        now = now.plus(600, ChronoUnit.MILLIS);
        map.remove("key1");
        Assert.assertArrayEquals(map.values().toArray(), cache.values().toArray());
        Assert.assertArrayEquals(map.keySet().toArray(), cache.keySet().toArray());
        Assert.assertArrayEquals(map.entrySet().toArray(), cache.entrySet().toArray());
    }

}