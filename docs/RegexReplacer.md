# JSoftware utils - RegexReplacer

Replace regexp pattern using callback function.

### Usage:
```java
RegexReplacer regexReplacer = new RegexReplacer(Pattern.compile("\\s"));            // replace all white-space characters
String result = regexReplacer.replaceGroupsAll("Hello World! How are you?", (str) -> "__"); // with two underscores
```
