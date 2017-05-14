package org.jsoftware.utils.text;

import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author m-szalik
 */
public class RegexReplacer {
    private final Pattern pattern;

    /**
     * @param pattern patter to be used
     */
    public RegexReplacer(Pattern pattern) {
        this.pattern = pattern;
    }

    /**
     * Replace all regex groups
     * @param text search in string
     * @param replaceFunction replace function
     * @return replacement result
     */
    public String replaceGroupsAll(CharSequence text, Function<String,CharSequence> replaceFunction) {
        Matcher matcher = pattern.matcher(text);
        StringBuilder out = new StringBuilder();
        int start = 0;
        while(matcher.find(start)) {
            int p0 = matcher.start();
            out.append(text.subSequence(start, p0));
            out.append(replaceFunction.apply(matcher.group()));
            start = matcher.end();
        }
        out.append(text.subSequence(start, text.length()));
        return out.toString();
    }

}
