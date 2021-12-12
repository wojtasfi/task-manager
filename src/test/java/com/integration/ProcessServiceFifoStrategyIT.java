package com.integration;

import com.manager.application.strategies.AddProcessStrategyName;
import com.manager.domain.Process;
import com.manager.domain.Priority;
import com.manager.infrastructure.Request;
import com.manager.infrastructure.RequestType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProcessServiceFifoStrategyIT extends IntegrationTestSupport {

    @Test
    void should_add_process_with_fifo_strategy_when_list_not_full() {
        //given
        addProcess(Priority.LOW, 3);

        //when
        Request request = Request.builder()
                .requestType(RequestType.ADD)
                .addProcessStrategy(AddProcessStrategyName.FIFO)
                .priority(Priority.LOW)
                .build();

        executeAndWait(request);

        //then
        assertEquals(4, processService.listAllProcesses().size());

    }

    @Test
    void should_remove_oldest_process_when_capacity_full() {
        //given
        addProcess(Priority.LOW, 3);
        Process lastAdded = addProcess(Priority.LOW);

        //when
        Request request = Request.builder()
                .requestType(RequestType.ADD)
                .addProcessStrategy(AddProcessStrategyName.FIFO)
                .priority(Priority.LOW)
                .build();

        Process addedProcess = (Process) executeAndWait(request);

        //then
        List<Process> processes = processService.listAllProcesses();
        assertEquals(4, processes.size());
        assertFalse(processExists(lastAdded.getPID()));
        assertTrue(processExists(addedProcess.getPID()));
    }
}
