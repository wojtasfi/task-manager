package com.manager.application.strategies;

import com.manager.application.ProcessCoordinator;
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
import static org.mockito.BDDMockito.given;

public class FifoAddProcessStrategyTest {

    @Mock
    ProcessCoordinator processCoordinator;
    FifoAddProcessStrategy fifoAddProcessStrategy;

    @Captor
    public ArgumentCaptor<Priority> processCaptor;

    @Captor
    public ArgumentCaptor<Integer> processIdCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        given(processCoordinator.capacityReached()).willReturn(false);
        fifoAddProcessStrategy = new FifoAddProcessStrategy(processCoordinator);
    }

    @Test
    public void should_add_process() {
        //given
        Priority processToAddPriority = Priority.HIGH;

        //when
        fifoAddProcessStrategy.addProcess(processToAddPriority);

        //then
        Mockito.verify(processCoordinator).addProcess(processCaptor.capture());
        Priority addedProcessPriority = processCaptor.getValue();

        assertEquals(processToAddPriority, addedProcessPriority);
    }

    @Test
    public void should_remove_oldest_process_when_capacity_full() {
        //given
        Priority processToAddPriority = Priority.HIGH;
        Process existingProcessOne = Process.of(2, Priority.MEDIUM);
        Process existingProcessTwo = Process.of(3, Priority.LOW);

        given(processCoordinator.capacityReached()).willReturn(true);
        given(processCoordinator.getAllRunningProcesses()).willReturn(List.of(existingProcessOne, existingProcessTwo));

        //when
        fifoAddProcessStrategy.addProcess(processToAddPriority);

        //then
        Mockito.verify(processCoordinator).addProcess(processCaptor.capture());
        Mockito.verify(processCoordinator).kill(processIdCaptor.capture());
        Priority addedProcessPriority = processCaptor.getValue();
        Integer killedProcessId = processIdCaptor.getValue();

        assertEquals(processToAddPriority, addedProcessPriority);
        assertEquals(existingProcessTwo.getPID(), killedProcessId);

    }
}
