package org.jsoftware.utils.collection;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 * LRU (Least Recently Used) Map that keeps at max <code>capacity</code> number of elements
 * @author m-szalik
 */
public class LRUMap<K,V> extends LinkedHashMap<K,V> {
    private final int capacity;
    private final Queue<Object> keys;

    /**
     * LRU Map with maximum size of <tt>capacity</tt>
     * @param capacity maximum map's size
     */
    public LRUMap(int capacity) {
        super();
        this.capacity = capacity;
        this.keys = new LinkedList<>();
    }

    private void cleanup() {
        if (size() > capacity) {
            synchronized (keys) {
                while (size() > capacity) {
                    Object k = keys.poll();
                    if (k != null) {
                        remove(k);
                    }
                }
            }
        }
    }

    private void updateKey(Object key) {
        if (key != null) {
            synchronized (keys) {
                keys.remove(key);
                keys.add(key);
            }
        }
    }

    @Override
    public V put(K key, V value) {
        updateKey(key);
        V v = super.put(key, value);
        cleanup();
        return v;
    }

    @Override
    public V putIfAbsent(K key, V value) {
        updateKey(key);
        V v = super.putIfAbsent(key, value);
        cleanup();
        return v;
    }


    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        m.keySet().stream().forEach((key) -> {
            updateKey(key);
        });
        super.putAll(m);
        cleanup();
    }

    @Override
    public V get(Object key) {
        V v = super.get(key);
        if (v != null) {
            updateKey(key);
        }
        return v;
    }

    @Override
    public V getOrDefault(Object key, V defaultValue) {
        V v = super.getOrDefault(key, defaultValue);
        if (v != null && ! v.equals(defaultValue)) {
            updateKey(key);
        }
        return v;
    }

    /**
     * @return Map capacity
     */
    public int getCapacity() {
        return capacity;
    }

    @Override
    public final void clear() {
        super.clear();
        synchronized (keys) {
            keys.clear();
        }
    }

    @Override
    public final V remove(Object key) {
        return super.remove(key);
    }
}