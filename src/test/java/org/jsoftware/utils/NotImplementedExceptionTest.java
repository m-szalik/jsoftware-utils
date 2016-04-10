package org.jsoftware.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NotImplementedExceptionTest {

    @Test
    public void testConstructorDefault() throws Exception {
        NotImplementedException ex = new NotImplementedException();
        assertEquals(NotImplementedException.DEFAULT_MESSAGE, ex.getMessage());
    }

    @Test
    public void testConstructorNull() throws Exception {
        NotImplementedException ex = new NotImplementedException(null);
        assertEquals(NotImplementedException.DEFAULT_MESSAGE, ex.getMessage());
    }

    @Test
    public void testConstructorMessage() throws Exception {
        NotImplementedException ex = new NotImplementedException("message");
        assertEquals("message", ex.getMessage());
    }
}