# Lokalize (LK)

 Maven: [ ![Download](https://api.bintray.com/packages/lightningkite/com.lightningkite.krosslin/lokalize/images/download.svg) ](https://bintray.com/lightningkite/com.lightningkite.krosslin/lokalize/_latestVersion) 

Tools for showing data localized across platforms.

Especially implements time components.

```
repositories {
    maven { url 'https://dl.bintray.com/lightningkite/com.lightningkite.krosslin' }
    ...
}
...
dependencies {
    ...
    //Depending on the version you need
    api "com.lightningkite:lokalize-metadata:${lokalizeVersion}"
    api "com.lightningkite:lokalize-jvm:${lokalizeVersion}"
    api "com.lightningkite:lokalize-js:${lokalizeVersion}"
    api "com.lightningkite:lokalize-iosarm64:${lokalizeVersion}"
    api "com.lightningkite:lokalize-iosx64:${lokalizeVersion}"
    and more!
}
```

## Features

- `Locale.default`, which gives you a default locale.
    - `Locale.language`, which gives you a language code.
    - `Locale.languageVariant`, which gives you a language variant code.
    - `Locale.getTimeOffsetMilliseconds()`, which gives you the time zone offest.
    - `Locale.renderNumber()`, which renders a number to a string.
    - `Locale.renderDate()`, which renders a date to a string.
    - `Locale.renderTime()`, which renders a time to a string.
    - `Locale.renderDateTime()`, which renders a date time to a string.
    - `Locale.renderTimeStamp()`, which renders a time stamp.
- `Date`, which only contains a date
- `Time`, which only contains a time in the day
- `DateTime`, which combines both
- `TimeStamp`, which is always in UTC
    - `TimeStamp.now()`, which gets the current time
- `Duration`, which represents a duration in milliseconds
- `ShortDuration`, which represents a finer duration in nanoseconds
