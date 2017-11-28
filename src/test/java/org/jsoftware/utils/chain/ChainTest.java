package org.jsoftware.utils.chain;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

public class ChainTest {

    @Test
    public void executeContinued() throws Exception {
        Chain<StringBuilder> chain = new Chain<>((context, doChain) -> {
            context.append("A");
            doChain.doContinue(context);
            context.append("Z");
        }, (context, doChain) -> {
            context.append("B");
            doChain.doContinue(context);
        });

        StringBuilder context = new StringBuilder();
        chain.execute(context);
        Assert.assertEquals("ABZ", context.toString());
    }

    @Test
    public void executeBroken() throws Exception {
        Chain<StringBuilder> chain = new Chain<>((context, doChain) -> {
            context.append("A");
            context.append("Z");
        }, (context, doChain) -> {
            context.append("B");
            doChain.doContinue(context);
        });

        StringBuilder context = new StringBuilder();
        chain.execute(context);
        Assert.assertEquals("AZ", context.toString());
    }

    @Test
    public void executeWithException() throws Exception {
        Chain<StringBuilder> chain = new Chain<>((context, doChain) -> {
            context.append("A");
            doChain.doContinue(context);
            context.append("Z");
        }, (context, doChain) -> {
            throw new IllegalStateException();
        });

        StringBuilder context = new StringBuilder();
        try {
            chain.execute(context);
            Assert.fail("Exception should be thrown!");
        } catch (IllegalStateException e) {
            // ok
        }
        Assert.assertEquals("A", context.toString());
    }

}