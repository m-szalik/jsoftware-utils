package org.jsoftware.utils;

/**
 * Holds an Object
 * @author szalik
 */
public final class Holder<T> {
    private volatile T object;

    public Holder() {
    }

    public Holder(T object) {
        this();
        this.object = object;
    }

    public synchronized T get() {
        return this.object;
    }

    public synchronized T set(T object) {
        T old = this.object;
        this.object = object;
        return old;
    }

}
