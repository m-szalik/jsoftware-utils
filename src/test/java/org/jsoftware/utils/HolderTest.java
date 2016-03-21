package org.jsoftware.utils;

import org.junit.Assert;
import org.junit.Test;

public class HolderTest {

    @Test
    public void testSetGet() throws Exception {
        Object o1s = new Object();
        Holder<Object> holder = new Holder<>(o1s);
        Assert.assertSame(o1s, holder.get());
        Object o2s = new Object();
        Object o1r = holder.set(o2s);
        Assert.assertSame(o1s, o1r);
        Assert.assertSame(o2s, holder.get());
    }
}