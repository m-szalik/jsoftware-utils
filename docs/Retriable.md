# JSoftware utils - Retriable

Retriable is an simple library to retry a code block if an exception should be raised. This is especially useful when interacting external api/services or file system calls.


### Simple usage:
```java
Future<String> future = Retriable.doTry(
    new Callable<String>() {
        @Override
        public String call() throws Exception {
            // Job to be done.
        },
    5       // retry limit
);

```


### Advanced usage - custom delay function:
```java
Future<String> future = Retriable.doTry(
    new Callable<String>() {
        @Override
        public String call() throws Exception {
            // Job to be done.
        },
    5,      // retry limit
    RetriableDelayFunction.constFunction(1000)  // how long to wait between tries (1000ms)
);

```
