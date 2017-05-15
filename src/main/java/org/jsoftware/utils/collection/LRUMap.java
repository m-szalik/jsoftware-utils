package org.jsoftware.utils.collection;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * LRU (Least Recently Used) Map
 * @author m-szalik
 */
public class LRUMap<K,V> extends LinkedHashMap<K,V> {
    private final int capacity;

    /**
     * LRU Map with maximum size of <tt>capacity</tt>
     * @param capacity maximum map's size
     */
    public LRUMap(int capacity) {
        super();
        this.capacity = capacity;
    }

    final protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > capacity;
    }

    /**
     * @return Map capacity
     */
    public int getCapacity() {
        return capacity;
    }
}