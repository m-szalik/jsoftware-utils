package org.jsoftware.utils.text;

import org.junit.Assert;
import org.junit.Test;

import java.util.function.Function;
import java.util.regex.Pattern;

public class RegexReplacerTest {
    private final Function<String, CharSequence> function = s -> "<" + s + ">";

    @Test
    public void testReplace1() throws Exception {
        RegexReplacer replacer = new RegexReplacer(Pattern.compile("([a-f])"));
        String result = replacer.replaceGroupsAll("Aaf123ffZ", function);
        Assert.assertEquals("A<a><f>123<f><f>Z", result);
    }

    @Test
    public void testReplace2() throws Exception {
        RegexReplacer replacer = new RegexReplacer(Pattern.compile("(.)"));
        String result = replacer.replaceGroupsAll("abc", function);
        Assert.assertEquals("<a><b><c>", result);
    }

    @Test
    public void testReplaceGreedy() throws Exception {
        /* Greedy quantifiers are considered "greedy" because they force the matcher to read in, or eat, the entire input string prior to attempting the first match.
           If the first match attempt (the entire input string) fails, the matcher backs off the input string by one character and tries again, repeating the process until
           a match is found or there are no more characters left to back off from. Depending on the quantifier used in the expression,
           the last thing it will try matching against is 1 or 0 characters.
        */
        RegexReplacer replacer = new RegexReplacer(Pattern.compile(".*foo")); // greedy quantifier
        String result = replacer.replaceGroupsAll("afoobarfoo", function);
        Assert.assertEquals("<afoobarfoo>", result);
    }

    @Test
    public void testReplaceReluctant() throws Exception {
        /* The reluctant quantifiers, however, take the opposite approach: They start at the beginning of the input string,
           then reluctantly eat one character at a time looking for a match. The last thing they try is the entire input string.
        */
        RegexReplacer replacer = new RegexReplacer(Pattern.compile(".*?foo")); // reluctant quantifier
        String result = replacer.replaceGroupsAll("afoobarfoo", function);
        Assert.assertEquals("<afoo><barfoo>", result);
    }

    @Test
    public void testReplacePossessive() throws Exception {
        /* Possessive quantifiers always eat the entire input string, trying once (and only once) for a match. Unlike the greedy quantifiers,
           possessive quantifiers never back off, even if doing so would allow the overall match to succeed.
        */
        RegexReplacer replacer = new RegexReplacer(Pattern.compile(".*+foo")); // possessive quantifier
        String result = replacer.replaceGroupsAll("afoobarfoo", function);
        Assert.assertEquals("afoobarfoo", result);
    }

    @Test
    public void testUperCase() throws Exception {
        RegexReplacer regexReplacer = new RegexReplacer(Pattern.compile("\\s"));
        String result = regexReplacer.replaceGroupsAll("Hello World! How are you?", (str) -> "__");
        Assert.assertEquals("Hello__World!__How__are__you?", result);
    }
}