package org.jsoftware.utils;


import java.time.Instant;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Simple cache based on LinkedHashMap
 * @author szalik
 */
public class SimpleCache<K,V> implements Map<K,V> {
    private final long timeoutMillis;
    private final SimpleCacheMap<K,CacheEntry<V>> cacheMap;

    /**
     * @param timeoutMillis cache ttl im milliseconds
     * @param cacheSize cache size
     */
    public SimpleCache(long timeoutMillis, int cacheSize) {
        this.timeoutMillis = timeoutMillis;
        this.cacheMap = new SimpleCacheMap<>(cacheSize);
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
            if ((value == null && ce.getValue() == null) || (value != null && value.equals(ce.getValue()) && isValid(ce))) {
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
        CacheEntry<V> ce = createOrGetEntry(key);
        ce.put(now().toEpochMilli() + timeoutMillis, value);
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
            CacheEntry<V> ce = createOrGetEntry(me.getKey());
            ce.put(now().toEpochMilli() + timeoutMillis, me.getValue());
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

    /**
     * Thread-safe method fetching cache object
     * @param key cache key
     * @param supplier object supplier
     * @return object form cache or produced by supplier
     */
    public V fetch(K key, Supplier<V> supplier) {
        CacheEntry<V> ce = createOrGetEntry(key);
        if (isValid(ce)) {
            return ce.getValue();
        } else {
            ce.updateValue(now().toEpochMilli() + timeoutMillis, supplier);
            return ce.getValue();
        }
    }

    private boolean isValid(CacheEntry<V> ce) {
        if (ce == null) {
            return false;
        }
        return ce.getTimeout() > now().toEpochMilli();
    }

    private CacheEntry<V> createOrGetEntry(K key) {
        synchronized (cacheMap) {
            CacheEntry<V> ce = cacheMap.get(key);
            if (ce == null) {
                ce = new CacheEntry<>();
                cacheMap.put(key, ce);
            }
            return ce;
        }
    }

    protected Instant now() {
        return Instant.now();
    }

}




class CacheEntry<V> {
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private volatile long timeout = Long.MIN_VALUE;
    private volatile V value;

    public long getTimeout() {
        return timeout;
    }

    public V getValue() {
        Lock rl = this.readWriteLock.readLock();
        rl.lock();
        try {
            return value;
        } finally {
            rl.unlock();
        }
    }

    public void updateValue(long timeout, Supplier<V> supplier) {
        Lock wl = this.readWriteLock.writeLock();
        if (! wl.tryLock()) {
            throw new IllegalStateException();
        }
        try {
            this.value = null;
            this.timeout = timeout;
            this.value = supplier.get();
        } finally {
            wl.unlock();
        }
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

    public void put(long timeout, V value) {
        this.timeout = timeout;
        this.value = value;
    }
}

class SimpleCacheMap<K,V> extends LinkedHashMap<K,V> {
    private final int cacheSize;

    SimpleCacheMap(int cacheSize) {this.cacheSize = cacheSize;}

    @Override
    protected boolean removeEldestEntry(Map.Entry eldest) {
        return this.size() >= cacheSize;
    }
}