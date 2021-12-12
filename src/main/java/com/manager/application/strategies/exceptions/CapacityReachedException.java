package com.manager.application.strategies.exceptions;

public class CapacityReachedException extends RuntimeException {

    private static final String MESSAGE_TEMPLATE = "Capacity reached";

    public CapacityReachedException() {
        super(MESSAGE_TEMPLATE);
    }
}
