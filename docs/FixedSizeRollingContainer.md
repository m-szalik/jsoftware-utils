# JSoftware utils - FixedSizeRollingContainer

Container that always holds <tt>size</tt> number of elements. It uses supplier to fill the container.

### Usage:
```java
FixedSizeRollingContainer<Integer> container = new FixedSizeRollingContainer<>(30, new Supplier<Integer>() {
			private int i = 0;
			@Override
			public Integer get() {
				return i++;
			}
		}); // create container with 30 elemens of Integer


container.swap(2, 3); // swap elements in container
List<Integer> list = container.getAsList();         // get elements from container as list

```
