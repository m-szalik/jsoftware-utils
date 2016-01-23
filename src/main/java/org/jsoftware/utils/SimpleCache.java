package org.jsoftware.utils;


import java.io.Serializable;
import java.time.Clock;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Simple cache based on LinkedHashMap
 * @author szalik
 */
public class SimpleCache<K,V> implements Map<K,V> {
    private final long timeoutMillis;
    private final Clock clock;
    private final LinkedHashMap<K,CacheEntry<V>> cacheMap;

    /**
     * @param timeoutMillis cache ttl im milliseconds
     * @param cacheSize cache size
     * @param clock to get <code>now</code>
     */
    protected SimpleCache(long timeoutMillis, int cacheSize, Clock clock) {
        this.timeoutMillis = timeoutMillis;
        this.clock = clock;
        this.cacheMap = new LinkedHashMap() {
            @Override
            protected boolean removeEldestEntry(Map.Entry eldest) {
                return size() >= cacheSize;
            }
        };
    }


    /**
     * @param timeoutMillis cache ttl im milliseconds
     * @param size cache size
     */
    public SimpleCache(long timeoutMillis, int size) {
        this(timeoutMillis, size, Clock.systemDefaultZone());
    }

    /**
     * Create cache with default size 128 entries
     * @param timeoutMillis cache ttl im milliseconds
     */
    public SimpleCache(long timeoutMillis) {
        this(timeoutMillis, 128);
    }

    @Override
    public int size() {
        return cacheMap.size();
    }

    @Override
    public boolean isEmpty() {
        return cacheMap.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return cacheMap.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        for(CacheEntry<V> ce : cacheMap.values()) {
            if ((value == null && ce.getValue() == null) || value.equals(ce.getValue()) && isValid(ce)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public V get(Object key) {
        CacheEntry<V> ce = cacheMap.get(key);
        return isValid(ce) ? ce.getValue() : null;
    }

    @Override
    public V put(K key, V value) {
        CacheEntry<V> ce = cacheMap.put(key, createEntry(value));
        return isValid(ce) ? ce.getValue() : null;
    }

    @Override
    public V remove(Object key) {
        CacheEntry<V> ce = cacheMap.remove(key);
        return isValid(ce) ? ce.getValue() : null;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        m.entrySet().forEach(me -> {
            cacheMap.put(me.getKey(), createEntry(me.getValue()));
        });
    }

    @Override
    public void clear() {
        cacheMap.clear();
    }

    @Override
    public Set<K> keySet() {
        return cacheMap.keySet();
    }

    @Override
    public Collection<V> values() {
        return cacheMap.values().stream().map(ce -> ce.getValue()).collect(Collectors.toList());
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        throw new NotImplementedException();
    }

    public V fetch(K key, Supplier<V> supplier) {
        CacheEntry<V> ce = cacheMap.get(key);
        if (isValid(ce)) {
            return ce.getValue();
        } else {
            V v = supplier.get();
            cacheMap.put(key, createEntry(v));
            return v;
        }
    }

    private boolean isValid(CacheEntry<V> ce) {
        if (ce == null) {
            return false;
        }
        return ce.getTimeout() < clock.millis();
    }

    private CacheEntry<V> createEntry(V value) {
        return new CacheEntry<>(clock.millis() + timeoutMillis, value);
    }

}




class CacheEntry<V> implements Serializable {
    private final long timeout;
    private final V value;

    CacheEntry(long timeout, V value) {
        this.timeout = timeout;
        this.value = value;
    }

    public long getTimeout() {
        return timeout;
    }

    public V getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        CacheEntry<?> that = (CacheEntry<?>) o;
        return !(value != null ? !value.equals(that.value) : that.value != null);

    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }
}