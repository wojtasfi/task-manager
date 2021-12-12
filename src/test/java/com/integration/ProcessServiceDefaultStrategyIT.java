package com.integration;

import com.manager.application.strategies.AddProcessStrategyName;
import com.manager.domain.Priority;
import com.manager.domain.Process;
import com.manager.infrastructure.Request;
import com.manager.infrastructure.RequestType;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class ProcessServiceDefaultStrategyIT extends IntegrationTestSupport {

    @Test
    void should_add_process_with_default_strategy_when_list_not_full() {
        //given
        addProcess(Priority.LOW);

        //when
        Request request = Request.builder()
                .requestType(RequestType.ADD)
                .addProcessStrategy(AddProcessStrategyName.DEFAULT)
                .priority(Priority.LOW)
                .build();

        executeAndWait(request);

        //then
        assertEquals(2, processService.listAllProcesses().size());

    }

    @Test
    void should_throw_when_adding_process_with_default_strategy_when_list_is_full() {
        //given
        addProcess(Priority.LOW, 4);

        //when
        Request request = Request.builder()
                .requestType(RequestType.ADD)
                .addProcessStrategy(AddProcessStrategyName.DEFAULT)
                .priority(Priority.LOW)
                .build();

        Process addedProcess = (Process) executeAndWait(request);

        //then
        assertEquals(4, processService.listAllProcesses().size());
        assertFalse(processExists(addedProcess.getPID()));
    }
}
