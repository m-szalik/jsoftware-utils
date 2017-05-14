# JSoftware utils - TestClock

Measure time of an operation or between two events.

### Usage:
```java
TestClock clock = new TestClock(ZoneId.of("UTC"));  // clock is set to t=now and will not change utils you do it.
clock.plus(+1, ChronoUnit.DAYS);                    // add one day to clock's time
// now the clock's time is set to t = t + 1 day
```
