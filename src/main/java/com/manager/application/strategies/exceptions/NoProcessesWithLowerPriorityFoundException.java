package com.manager.application.strategies.exceptions;

public class NoProcessesWithLowerPriorityFoundException extends RuntimeException {

    private static final String MESSAGE_TEMPLATE =
            "Process not added: capacity reached and there is no process with lower priority";

    public NoProcessesWithLowerPriorityFoundException() {
        super(MESSAGE_TEMPLATE);
    }
}
