# Lokalize (LK)

Tools for showing data localized across platforms.

Especially implements time components.

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
- `Duration`, which represents a duration in milliseconds
- `ShortDuration`, which represents a finer duration in nanoseconds