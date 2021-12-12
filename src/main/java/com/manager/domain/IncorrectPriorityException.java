package com.manager.domain;

import static java.lang.String.format;

public class IncorrectPriorityException extends RuntimeException {

    private static final String MESSAGE_TEMPLATE = "Priority %s does not exist";

    public IncorrectPriorityException(String wrongPriority) {
        super(format(MESSAGE_TEMPLATE, wrongPriority));
    }
}
