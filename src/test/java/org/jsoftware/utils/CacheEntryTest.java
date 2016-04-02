package org.jsoftware.utils;

import org.junit.Assert;
import org.junit.Test;

public class CacheEntryTest {

    @Test
    public void testEqualsEq() throws Exception {
        CacheEntry<Integer> cacheEntry1 = new CacheEntry<>();
        CacheEntry<Integer> cacheEntry2 = new CacheEntry<>();
        cacheEntry1.updateValue(100, () -> new Integer(4));
        cacheEntry2.updateValue(500, () -> new Integer(7));
        Assert.assertFalse(cacheEntry1.equals(cacheEntry2));
    }

    @Test
    public void testEqualsNotEq() throws Exception {
        CacheEntry<Integer> cacheEntry1 = new CacheEntry<>();
        CacheEntry<Integer> cacheEntry2 = new CacheEntry<>();
        cacheEntry1.updateValue(100, () -> new Integer(12));
        cacheEntry2.updateValue(500, () -> new Integer(12));
        Assert.assertEquals(cacheEntry1, cacheEntry2);
    }

    @Test
    public void testEqualsNull() throws Exception {
        CacheEntry<Integer> cacheEntry1 = new CacheEntry<>();
        CacheEntry<Integer> cacheEntry2 = new CacheEntry<>();
        cacheEntry1.updateValue(100, () -> null);
        cacheEntry2.updateValue(500, () -> null);
        Assert.assertEquals(cacheEntry1, cacheEntry2);
    }

    @Test
    public void testHashcode() throws Exception {
        CacheEntry<Integer> cacheEntry1 = new CacheEntry<>();
        CacheEntry<Integer> cacheEntry2 = new CacheEntry<>();
        cacheEntry1.updateValue(100, () -> new Integer(12));
        cacheEntry2.updateValue(500, () -> new Integer(12));
        Assert.assertEquals(cacheEntry1.hashCode(), cacheEntry2.hashCode());
    }

}