package org.jsoftware.utils.text;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class JoinerTest {

    @Test
    public void testJoinArray() throws Exception {
        String result = Joiner.on(',').join(new String[] { "a", "bb", "ccc" });
        Assert.assertEquals("a,bb,ccc", result);
    }

    @Test
    public void testJoinCollection() throws Exception {
        String result = Joiner.on(',').join(Arrays.asList("a", "bb", "ccc"));
        Assert.assertEquals("a,bb,ccc", result);
    }

    @Test
    public void testJoinNullElements() throws Exception {
        String result;
        result = Joiner.on(',').join(Arrays.asList("a", null, "ccc"));
        Assert.assertEquals("a,null,ccc", result);

        result = Joiner.on(',').join(new String[] { "a", null, "ccc" });
        Assert.assertEquals("a,null,ccc", result);
    }


}