package org.jsoftware.utils;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Holds an Object
 * @author szalik
 */
public final class Holder<T> {
    private final Lock writeLock = new ReentrantLock();
    private volatile T object;

    public Holder() {
    }

    public Holder(T object) {
        this();
        this.object = object;
    }

    public T get() {
        return this.object;
    }

    public T set(T object) {
        writeLock.lock();
        T old = this.object;
        this.object = object;
        writeLock.unlock();
        return old;
    }

}
