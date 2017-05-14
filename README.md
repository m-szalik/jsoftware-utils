# JSoftware utils 

[![Build Status](https://travis-ci.org/m-szalik/jsoftware-utils.svg?branch=master)](https://travis-ci.org/m-szalik/jsoftware-utils)
[![codecov.io](https://codecov.io/github/m-szalik/jsoftware-utils/coverage.svg?branch=master)](https://codecov.io/github/m-szalik/jsoftware-utils?branch=master)
[![Dependency Status](https://www.versioneye.com/user/projects/56e2b440df573d003a5f5bf5/badge.svg?style=flat)](https://www.versioneye.com/user/projects/56e2b440df573d003a5f5bf5)
[![Codacy Badge](https://api.codacy.com/project/badge/grade/c45e6725b6b7477ea8e041f3aefd5bc5)](https://www.codacy.com/app/szalik/jsoftware-utils)

## Common tools and helpers like:
 * Fixed size value containers
   * [FixedSizeRollingContainer](docs/FixedSizeRollingContainer.md) - Container that always holds x number of elements. It uses supplier to fill the container.
   * [FixedSizeRollingList](docs/FixedSizeRollingList.md) - Remove first element if list capacity is exceeded.
 * [MimeType resolver](docs/MimeTypeResolver.md) - resolve a file mime type (deprecated)
 * Text manipulation utils
   * [RegexReplacer](docs/RegexReplacer.md) - Replace regexp pattern using callback function.
 * Time measurement
   * [TimeWatch](docs/TimeWatch.md) - Measure time of an operation.
   * [MeasureHitsInPeriod](docs/MeasureHitsInPeriod.md) - Measure amount of hits in defined period.
   * [TestClock](docs/TestClock.md) - A java.time.Clock that ticks when it's required.
 * [SimpleCache](docs/SimpleCache.md) - Very simple caching.
 * [Retriable](docs/Retriable.md) - Retriable is an simple library to retry a code block if an exception has been raised.


## Requirements
 * Java 8 or newer

## License

Apache License 2.0