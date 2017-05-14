# JSoftware utils - TimeWatch

Measure time of an operation or between two events.

### Usage:
```java
TimeWatch timeWatch = new TimeWatch();
Thread.sleep(10000);    // long operation here....
timeWatch.stop();
long ts = timeWatch.getDuration();  // operation time in ms
System.out.println(timeWatch.getDurationHuman()); // humen frendly output
```
