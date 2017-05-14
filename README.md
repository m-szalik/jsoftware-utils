# JSoftware utils 

[![Build Status](https://travis-ci.org/m-szalik/jsoftware-utils.svg?branch=master)](https://travis-ci.org/m-szalik/jsoftware-utils)
[![codecov.io](https://codecov.io/github/m-szalik/jsoftware-utils/coverage.svg?branch=master)](https://codecov.io/github/m-szalik/jsoftware-utils?branch=master)
[![Dependency Status](https://www.versioneye.com/user/projects/56e2b440df573d003a5f5bf5/badge.svg?style=flat)](https://www.versioneye.com/user/projects/56e2b440df573d003a5f5bf5)
[![Codacy Badge](https://api.codacy.com/project/badge/grade/c45e6725b6b7477ea8e041f3aefd5bc5)](https://www.codacy.com/app/szalik/jsoftware-utils)

## Common tools and helpers like:
 * Fixed size lists
 * MimeType resolver
 * Text manipulation utils
 * Time watch
 * Simple cache
 * Retriable


## Requirements
 * Java 8 or newer

## Usage

### Retriable

Simple:
```
Future<String> future = Retriable.doTry(
    new Callable<String>() {
        @Override
        public String call() throws Exception {
            // Job to be done.
        },
    5       // retry limit
);

```


Advanced - custom wait function:
```
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
