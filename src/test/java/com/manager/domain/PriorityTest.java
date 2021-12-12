package com.manager.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PriorityTest {

    private final static boolean notLower = false;
    private final static boolean lower = true;

    private static Stream<Arguments> testData() {
        return Stream.of(
                Arguments.of(Priority.LOW, Priority.MEDIUM, lower),
                Arguments.of(Priority.LOW, Priority.HIGH, lower),
                Arguments.of(Priority.MEDIUM, Priority.HIGH, lower),
                Arguments.of(Priority.HIGH, Priority.HIGH, notLower),
                Arguments.of(Priority.HIGH, Priority.LOW, notLower),
                Arguments.of(Priority.HIGH, Priority.MEDIUM, notLower)
        );
    }

    @ParameterizedTest
    @MethodSource("testData")
    void should_have_lower_priority(Priority priorityOne, Priority priorityTwo, boolean lower) {
       assertEquals(lower, priorityOne.isLowerThan(priorityTwo));
    }
}
