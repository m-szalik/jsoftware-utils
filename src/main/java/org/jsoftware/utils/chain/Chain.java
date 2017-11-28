package org.jsoftware.utils.chain;


/**
 * Chain responsibility pattern implementation.
 * @author m-szalik
 * @param <C> invocation context type
 */
public class Chain<C> {
    private final ChainCommandHolder<C> first;

    public Chain(ChainCommand<C>... commands) {
        if (commands != null && commands.length > 0) {
            ChainCommandHolder<C> prev = null;
            for (int i = commands.length -1; i >= 0; i--) {
                ChainCommand command = commands[i];
                ChainCommandHolder<C> current = new ChainCommandHolder<>(command, prev);
                prev = current;
            }
            first = prev;
        } else {
            first = null;
        }
    }

    public void execute(C context) throws Exception {
        if (first != null) {
            first.execute(context);
        }
    }
}

/**
 * Holds command and it's successor
 * @param <C> Invocation context type
 */
class ChainCommandHolder<C> implements DoChain<C> {
    private final ChainCommand<C> command;
    private final ChainCommandHolder<C> next;

    public ChainCommandHolder(ChainCommand<C> command, ChainCommandHolder<C> next) {
        this.command = command;
        this.next = next;
    }

    @Override
    public void doContinue(C context) throws Exception {
        if (next != null) {
            next.execute(context);
        }
    }

    void execute(C context) throws Exception {
        command.execute(context, this);
    }
}