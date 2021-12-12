package com.manager.infrastructure.process;

import static java.lang.String.format;

public class ProcessNotFoundException extends RuntimeException {

    private static final String MESSAGE_TEMPLATE = "Process with PID %s does not exist";

    public ProcessNotFoundException(Integer processId) {
        super(format(MESSAGE_TEMPLATE, processId.toString()));
    }

}
