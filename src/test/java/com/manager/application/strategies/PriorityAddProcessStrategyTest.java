package com.manager.application.strategies;

import com.manager.application.ProcessCoordinator;
import com.manager.application.strategies.exceptions.NoProcessesWithLowerPriorityFoundException;
import com.manager.domain.Priority;
import com.manager.domain.Process;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Captor;
import org.mockito.ArgumentCaptor;
import org.mockito.MockitoAnnotations;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;

public class PriorityAddProcessStrategyTest {

    @Mock
    ProcessCoordinator processCoordinator;
    PriorityAddProcessStrategy priorityAddProcessStrategy;

    @Captor
    public ArgumentCaptor<Priority> processCaptor;

    @Captor
    public ArgumentCaptor<Integer> processIdCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        given(processCoordinator.capacityReached()).willReturn(false);
        priorityAddProcessStrategy = new PriorityAddProcessStrategy(processCoordinator);
    }

    @Test
    public void should_add_process() {
        //given
        Priority processPriorityToAdd = Priority.HIGH;

        //when
        priorityAddProcessStrategy.addProcess(processPriorityToAdd);

        //then
        Mockito.verify(processCoordinator).addProcess(processCaptor.capture());
        Priority addedProcessPriority = processCaptor.getValue();

        assertEquals(processPriorityToAdd, addedProcessPriority);
    }

    @Test
    public void should_remove_oldest_process_with_lower_priority_when_capacity_reached() {
        //given
        Priority processPriorityToAdd = Priority.HIGH;
        Process existingProcessWithLowestPriorityOlder = Process.of(1, Priority.MEDIUM);
        Process existingProcessWithLowerPriority = Process.of(2, Priority.MEDIUM);
        Process existingProcessWithSamePriority = Process.of(3, Priority.HIGH);

        given(processCoordinator.capacityReached()).willReturn(true);
        given(processCoordinator.getAllRunningProcesses()).willReturn(List.of(
                existingProcessWithLowestPriorityOlder, existingProcessWithLowerPriority, existingProcessWithSamePriority
        ));

        //when
        priorityAddProcessStrategy.addProcess(processPriorityToAdd);

        //then
        Mockito.verify(processCoordinator).addProcess(processCaptor.capture());
        Mockito.verify(processCoordinator, times(1)).kill(processIdCaptor.capture());
        Priority addedProcessPriority = processCaptor.getValue();
        Integer killedProcessId = processIdCaptor.getValue();

        assertEquals(processPriorityToAdd, addedProcessPriority);
        assertEquals(existingProcessWithLowestPriorityOlder.getPID(), killedProcessId);

    }

    @Test
    public void should_throw_when_capacity_reached_and_no_process_with_lower_priority_exists() {
        //given
        Priority processPriorityToAdd = Priority.MEDIUM;
        Process existingProcessWithSamePriority = Process.of(1, Priority.MEDIUM);
        Process existingProcessWithHigherPriority = Process.of(2, Priority.HIGH);

        given(processCoordinator.capacityReached()).willReturn(true);
        given(processCoordinator.getAllRunningProcesses())
                .willReturn(List.of(existingProcessWithSamePriority, existingProcessWithHigherPriority));

        //when
        Exception exception = assertThrows(NoProcessesWithLowerPriorityFoundException.class, () ->
                priorityAddProcessStrategy.addProcess(processPriorityToAdd)
        );

        //then
        assertEquals(
                exception.getMessage(),
                "Process not added: capacity reached and there is no process with lower priority"
        );

    }
}
