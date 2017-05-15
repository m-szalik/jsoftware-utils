package org.jsoftware.utils.collection;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

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
        map.get("2");
        map.put("5", 5);
        Assert.assertArrayEquals(new String[] {"2", "4", "5"}, map.keySet().toArray());
    }

    @Test
    public void testClear() throws Exception {
        map.put("1", 1);
        map.put("2", 2);
        Assert.assertEquals(2, map.size());
        map.clear();
        Assert.assertEquals(0, map.size());
    }

    @Test(timeout = 100L)
    public void testPerformance() throws Exception {
        List<Integer> found = new LinkedList<>();
        for(int i=0; i<10000; i++) {
            map.put(Integer.toString(i), i);
        }
        for(int i=10000; i>0; i--) {
            Integer v = map.get(Integer.toString(i));
            if (v != null) {
                found.add(v);
            }
        }
        Assert.assertArrayEquals(new Integer[] {9999, 9998, 9997}, found.toArray());
    }
}