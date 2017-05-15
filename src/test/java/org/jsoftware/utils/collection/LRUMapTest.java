package org.jsoftware.utils.collection;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class LRUMapTest {
    private LRUMap<String, Integer> map;

    @Before
    public void setUp() throws Exception {
        map = new LRUMap<>(3);
    }

    @Test
    public void testCapacity() throws Exception {
        Assert.assertEquals(3, map.getCapacity());
        Assert.assertEquals(0, map.size());
        map.put("1", 1);
        map.put("2", 2);
        map.put("3", 3);
        Assert.assertEquals(3, map.size());
        map.put("4", 4);
        Assert.assertEquals(3, map.size());
        Assert.assertEquals(3, map.getCapacity());
    }

    @Test
    public void testLRU() throws Exception {
        map.put("1", 1);
        map.put("2", 2);
        map.put("3", 3);
        map.put("4", 4);
        Assert.assertArrayEquals(new String[] {"2", "3", "4"}, map.keySet().toArray());
        Assert.assertArrayEquals(new Integer[] {2, 3, 4}, map.values().toArray());
    }
}