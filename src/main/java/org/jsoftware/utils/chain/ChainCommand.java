package org.jsoftware.utils.chain;

/**
 * Chain command
 * @see Chain
 * @author m-szalik
 * @param <C> invocation context type
 * <p>Example:
 *    <code>
 *
 *    </code>
 * </p>
 */
public interface ChainCommand<C> {

    /**
     * @param context invocation context
     * @param doChain invoke next command in chain
     * @throws Exception any exception
     */
    void execute(C context, DoChain<C> doChain) throws Exception;

}
