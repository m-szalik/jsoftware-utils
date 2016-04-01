package org.jsoftware.utils;

import org.junit.Assert;
import org.junit.Test;

public class NotImplementedExceptionTest {

    @Test
    public void testConstructorDefault() throws Exception {
        NotImplementedException ex = new NotImplementedException();
        Assert.assertEquals(NotImplementedException.DEFAULT_MESSAGE, ex.getMessage());
    }

    @Test
    public void testConstructorNull() throws Exception {
        NotImplementedException ex = new NotImplementedException(null);
        Assert.assertEquals(NotImplementedException.DEFAULT_MESSAGE, ex.getMessage());
    }

    @Test
    public void testConstructorMessage() throws Exception {
        NotImplementedException ex = new NotImplementedException("message");
        Assert.assertEquals("message", ex.getMessage());
    }
}