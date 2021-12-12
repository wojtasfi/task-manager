package com.manager.application.strategies;

import com.manager.application.ProcessCoordinator;
import com.manager.application.strategies.exceptions.CapacityReachedException;
import com.manager.domain.Priority;
import com.manager.domain.Process;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

public class DefaultAddProcessStrategyTest {

    @Mock
    ProcessCoordinator processCoordinator;
    DefaultAddProcessStrategy defaultAddProcessStrategy;

    @Captor
    public ArgumentCaptor<Priority> processCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        given(processCoordinator.capacityReached()).willReturn(false);
        defaultAddProcessStrategy = new DefaultAddProcessStrategy(processCoordinator);
    }

    @Test
    public void should_add_process() {
        //given
        Priority newProcessPriority = Priority.LOW;

        //when
        defaultAddProcessStrategy.addProcess(newProcessPriority);

        //then
        Mockito.verify(processCoordinator).addProcess(processCaptor.capture());
        Priority addedProcessPriority = processCaptor.getValue();

        assertEquals(newProcessPriority, addedProcessPriority);
    }

    @Test
    public void should_throw() {
        //given
        given(processCoordinator.capacityReached()).willReturn(true);

        //when
        Exception exception = assertThrows(CapacityReachedException.class, () ->
                defaultAddProcessStrategy.addProcess(Priority.HIGH)
        );

        assertEquals(exception.getMessage(), "Capacity reached");

    }
}
