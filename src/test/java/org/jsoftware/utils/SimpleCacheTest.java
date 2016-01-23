package org.jsoftware.utils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.Instant;

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
}