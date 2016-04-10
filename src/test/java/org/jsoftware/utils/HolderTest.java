package org.jsoftware.utils;

import org.junit.Test;

import static org.junit.Assert.assertSame;

public class HolderTest {

    @Test
    public void testSetGet() throws Exception {
        Object o1s = new Object();
        Holder<Object> holder = new Holder<>(o1s);
        assertSame(o1s, holder.get());
        Object o2s = new Object();
        Object o1r = holder.set(o2s);
        assertSame(o1s, o1r);
        assertSame(o2s, holder.get());
    }
}