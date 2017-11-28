package org.jsoftware.utils.chain;

/**
 * Chain command
 * @see Chain
 * @author m-szalik
 * @param <C> invocation context type
 * <p>Example:
 *    <code>
 * class MyChainCommand implements ChainCommand<Context> {
 *  @Override
 *  public void execute(Context context, DoChain<Context> doChain) throws Exception {
 *   // do something before
 *   doChain.doContinue(context); // execute successors
 *   // do something after
 *  }
 *}
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
