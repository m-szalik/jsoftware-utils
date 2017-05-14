# JSoftware utils - FixedSizeRollingList

A list that always holds at max fixedSize of elements. If more elements are added the first one is automatically removed.

### Usage:
```java
List<Integer> list = new FixedSizeRollingList<>(2); // fixed size list
list.addAll(Arrays.asList(0, 1));   // list's elements are 0,1
list.add(2);						// list's elements are now 1,2
```
