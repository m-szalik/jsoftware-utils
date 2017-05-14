package org.jsoftware.utils.cache;

import java.util.Map;
import java.util.function.Supplier;

/**
 * Simple cache interface
 * @author m-szalik
 */
public interface Cache<K,V> extends Map<K,V> {

    /**
     * Thread-safe method fetching cache object
     * @param key cache key
     * @param supplier object supplier
     * @return object form cache or produced by supplier
     */
    V fetch(K key, Supplier<V> supplier);

}