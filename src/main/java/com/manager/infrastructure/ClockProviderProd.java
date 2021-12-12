package com.manager.infrastructure;

import java.time.Clock;

public class ClockProviderProd implements ClockProvider {

    private final Clock clock = Clock.systemDefaultZone();

    @Override
    public Clock getClock() {
        return this.clock;
    }
}
