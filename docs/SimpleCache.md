# JSoftware utils - SimpleCache

SimpleCache is a map with TTL and maximum capacity.

### Usage:
```java
Cache<String,Object> cache = new SimpleCache<>(
    TimeUnit.SECONDS.toMillis(1),   // TTL of entry
    300                             // cache capacity
);

cache.fetch(
    "x",                    // cache key
    () -> new Object()      // a supplier of value if not found in cache
);
```
