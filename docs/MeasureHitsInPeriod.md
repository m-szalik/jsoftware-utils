# JSoftware utils - MeasureHitsInPeriod

Measure hits in defined period of time.

### Usage:
```java
MeasureHitsInPeriod measure = new MeasureHitsInPeriod(TimeUnit.MINUTES.toMillis(1)); // define period (1 minute)
measure.hit();              // report a hit
measure.hit();              // report a hit
measure.get();              // returns 2

// wait 1 minute
measure.get();              // returns 0
```
