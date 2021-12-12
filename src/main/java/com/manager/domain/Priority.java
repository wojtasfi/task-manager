package com.manager.domain;

import lombok.Getter;

import java.util.stream.Stream;

public enum Priority {
    LOW("low", 1),
    MEDIUM("medium", 2),
    HIGH("high", 3);

    @Getter
    private final String stringName;

    @Getter
    private final Integer priorityOrder;

    Priority(String stringName, Integer priorityOrder) {
        this.stringName = stringName;
        this.priorityOrder = priorityOrder;
    }

    public Priority getForString(String stringName) {
        return Stream.of(Priority.values())
                .filter(priority -> priority.getStringName().equals(stringName))
                .findFirst()
                .orElseThrow(() -> new IncorrectPriorityException(stringName));
    }

    public boolean isLowerThan(Priority otherPriority) {
        Priority currentPriority = this.getForString(stringName);
        return currentPriority.getPriorityOrder() < otherPriority.getPriorityOrder();
    }
}
