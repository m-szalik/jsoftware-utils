package org.jsoftware.utils;


import org.junit.Test;

public class TimeWatchTest {

    @Test
    public void testMeasure() throws InterruptedException {
        TimeWatch timeWatch = TimeWatch.newInstance();
        Thread.sleep(1200);
        String ts = timeWatch.getDurationHuman();
//        System.out.println("Output: " + ts);
        org.junit.Assert.assertTrue(ts.matches("\\d{4}ms\\."));
    }
}
