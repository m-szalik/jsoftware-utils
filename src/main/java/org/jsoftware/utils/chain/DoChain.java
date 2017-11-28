package org.jsoftware.utils.chain;

/**
 * Next command in chain invocation.
 * @author m-szalik
 * @param <C> Invocation context type
 */
public interface DoChain<C> {

    /**
     * Invoke next command
     * @param context context of an invocation
     * @throws Exception any exception thrown by following commands in chain
     */
    void doContinue(C context) throws Exception;

}
