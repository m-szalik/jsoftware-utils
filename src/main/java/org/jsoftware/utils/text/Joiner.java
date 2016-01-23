package org.jsoftware.utils.text;

import java.util.Collection;
import java.util.Iterator;

/**
 * Join elements
 * @author szalik
 */
public final class Joiner {
    private final String join;

    /**
     * @param join joiner
     */
    private Joiner(String join) {
        this.join = join;
    }

    /**
     * @param join joiner
     * @return Joiner
     */
    public static Joiner on(char join) {
        return new Joiner(Character.toString(join));
    }


    /**
     * @param join joiner
     * @return Joiner
     */
    public static Joiner on(CharSequence join) {
        return new Joiner(join.toString());
    }


    /**
     * @param elements elements to join
     * @return join result
     */
    public String join(Object[] elements) {
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<elements.length; i++) {
            sb.append(elements[i]);
            if(i+1 < elements.length) {
                sb.append(join);
            }
        }
        return sb.toString();
    }


    /**
     * @param elements elements to join
     * @return join result
     */
    public String join(Collection<Object> elements) {
        StringBuilder sb = new StringBuilder();
        Iterator<Object> it = elements.iterator();
        while (it.hasNext()) {
            sb.append(it.next());
            if(it.hasNext()) {
                sb.append(join);
            }
        }
        return sb.toString();
    }

}
