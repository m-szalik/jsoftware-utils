package org.jsoftware.utils.retriable;

import org.junit.Assert;
import org.junit.Test;

public class RetriableDelayFunctionTest {

    @Test
    public void testLinear() throws Exception {
        RetriableDelayFunction function = RetriableDelayFunction.linearFunction(10);
        for(int i=0; i<10; i++) {
            Assert.assertEquals(10*i, function.retryWait(i));
        }
    }

    @Test
    public void testConst() throws Exception {
        RetriableDelayFunction function = RetriableDelayFunction.constFunction(10);
        for(int i=0; i<10; i++) {
            Assert.assertEquals(10, function.retryWait(i));
        }
    }

    @Test
    public void testNoDely() throws Exception {
        RetriableDelayFunction function = RetriableDelayFunction.noDealyFunction();
        for(int i=0; i<10; i++) {
            Assert.assertEquals(0, function.retryWait(i));
        }
    }
}