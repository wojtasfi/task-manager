package com.integration;

import com.manager.infrastructure.ClockProvider;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;

public class TestClockProvider implements ClockProvider {

    private Clock fixedClock;
    public TestClockProvider(LocalDateTime localDateTime) {
        this.initializeFakeClock(localDateTime);
    }

    @Override
    public Clock getClock() {
        return this.fixedClock;
    }

    public Clock overrideTime(LocalDateTime newLocalDateTime) {
        initializeFakeClock(newLocalDateTime);
        return this.fixedClock;
    }

    private void initializeFakeClock(LocalDateTime localDateTime) {
        this.fixedClock = Clock.fixed(localDateTime.toInstant(ZoneOffset.UTC), ZoneId.of("UTC"));
    }

}
