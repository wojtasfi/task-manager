package com.integration;

import com.manager.application.strategies.AddProcessStrategyName;
import com.manager.domain.Priority;
import com.manager.domain.Process;
import com.manager.infrastructure.Request;
import com.manager.infrastructure.RequestType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProcessServicePriorityStrategyIT extends IntegrationTestSupport {

    @Test
    void should_add_process_with_fifo_strategy_when_list_not_full() {
        //given
        addProcess(Priority.LOW, 3);

        //when
        Request request = Request.builder()
                .requestType(RequestType.ADD)
                .addProcessStrategy(AddProcessStrategyName.PRIORITY)
                .priority(Priority.LOW)
                .build();

        executeAndWait(request);

        //then
        assertEquals(4, processService.listAllProcesses().size());
    }

    @Test
    void should_remove_oldest_with_lower_priority_process_when_capacity_full() {
        //given
        Process oldestWithLowerPriority = addProcess(Priority.LOW);
        addProcess(Priority.LOW);
        addProcess(Priority.MEDIUM);
        addProcess(Priority.HIGH);

        //when
        Request request = Request.builder()
                .requestType(RequestType.ADD)
                .addProcessStrategy(AddProcessStrategyName.PRIORITY)
                .priority(Priority.MEDIUM)
                .build();

        Process addedProcess = (Process) executeAndWait(request);

        //then
        List<Process> processes = processService.listAllProcesses();
        assertEquals(4, processes.size());
        assertFalse(processExists(oldestWithLowerPriority.getPID()));
        assertTrue(processExists(addedProcess.getPID()));
    }

    @Test
    void should_remove_oldest_with_lowest_priority_process_when_capacity_full() {
        //given
        Process oldestWithLowerPriority = addProcess(Priority.LOW);
        addProcess(Priority.LOW);
        addProcess(Priority.MEDIUM);
        addProcess(Priority.HIGH);

        //when
        Request request = Request.builder()
                .requestType(RequestType.ADD)
                .addProcessStrategy(AddProcessStrategyName.PRIORITY)
                .priority(Priority.HIGH)
                .build();

        Process addedProcess = (Process) executeAndWait(request);

        //then TEN
        List<Process> processes = processService.listAllProcesses();
        assertEquals(4, processes.size());
        assertFalse(processExists(oldestWithLowerPriority.getPID()));
        assertTrue(processExists(addedProcess.getPID()));
    }

    @Test
    void should_remove_oldest_with_lowest_priority_process_when_capacity_full_only_high_and_low_priority_on_list() {
        //given
        Process oldestWithLowerPriority = addProcess(Priority.LOW);
        addProcess(Priority.LOW);
        addProcess(Priority.HIGH);
        addProcess(Priority.HIGH);

        //when
        Request request = Request.builder()
                .requestType(RequestType.ADD)
                .addProcessStrategy(AddProcessStrategyName.PRIORITY)
                .priority(Priority.HIGH)
                .build();

        Process addedProcess = (Process) executeAndWait(request);

        //then
        List<Process> processes = processService.listAllProcesses();
        assertEquals(4, processes.size());
        assertFalse(processExists(oldestWithLowerPriority.getPID()));
        assertTrue(processExists(addedProcess.getPID()));
    }

    @Test
    void should_remove_oldest_with_lowest_priority_process_when_capacity_full_only_medium_priority_on_list() {
        //given
        Process oldestWithLowerPriority = addProcess(Priority.MEDIUM);
        addProcess(Priority.MEDIUM);
        addProcess(Priority.MEDIUM);
        addProcess(Priority.MEDIUM);

        //when
        Request request = Request.builder()
                .requestType(RequestType.ADD)
                .addProcessStrategy(AddProcessStrategyName.PRIORITY)
                .priority(Priority.HIGH)
                .build();

        Process addedProcess = (Process) executeAndWait(request);

        //then
        List<Process> processes = processService.listAllProcesses();
        assertEquals(4, processes.size());
        assertFalse(processExists(oldestWithLowerPriority.getPID()));
        assertTrue(processExists(addedProcess.getPID()));
    }

    @Test
    void should_throw_when_process_with_lower_priority_not_exists_when_capacity_full() {
        //given
        addProcess(Priority.MEDIUM, 4);

        //when
        Request request = Request.builder()
                .requestType(RequestType.ADD)
                .addProcessStrategy(AddProcessStrategyName.PRIORITY)
                .priority(Priority.MEDIUM)
                .build();

        Process addedProcess = (Process) executeAndWait(request);

        //then
        List<Process> processes = processService.listAllProcesses();
        assertEquals(4, processes.size());
        assertFalse(processExists(addedProcess.getPID()));
    }
}
