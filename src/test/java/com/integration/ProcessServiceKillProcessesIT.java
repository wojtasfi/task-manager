package com.integration;

import com.manager.domain.Priority;
import com.manager.domain.Process;
import com.manager.infrastructure.Request;
import com.manager.infrastructure.RequestType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class ProcessServiceKillProcessesIT extends IntegrationTestSupport {

    @Test
    void should_kill_process() {
        //given
        addProcess(Priority.LOW);
        addProcess(Priority.LOW);
        Process processToKill = addProcess(Priority.LOW);

        //when
        Request request = Request.builder()
                .requestType(RequestType.KILL)
                .processId(processToKill.getPID())
                .build();

        executeAndWait(request);

        //then
        assertEquals(2, processService.listAllProcesses().size());
        assertFalse(processExists(processToKill.getPID()));
    }

    @Test
    void should_kill_group_of_processes_with_priority_medium() {
        //given
        addProcess(Priority.LOW);
        addProcess(Priority.LOW);
        Process processToKill = addProcess(Priority.MEDIUM);
        addProcess(Priority.HIGH);

        //when
        Request request = Request.builder()
                .requestType(RequestType.KILL_GROUP)
                .priority(Priority.MEDIUM)
                .build();

        executeAndWait(request);

        //then
        assertEquals(3, processService.listAllProcesses().size());
        assertFalse(processExists(processToKill.getPID()));
    }

    @Test
    void should_kill_group_of_processes_with_priority_low() {
        //given
        Process processToKillOne = addProcess(Priority.LOW);
        Process processToKillTwo = addProcess(Priority.LOW);
        addProcess(Priority.MEDIUM);
        addProcess(Priority.HIGH);

        //when
        Request request = Request.builder()
                .requestType(RequestType.KILL_GROUP)
                .priority(Priority.LOW)
                .build();

        executeAndWait(request);

        //then
        assertEquals(2, processService.listAllProcesses().size());
        assertFalse(processExists(processToKillOne.getPID()));
        assertFalse(processExists(processToKillTwo.getPID()));
    }

    @Test
    void should_kill_all_processes() {
        //given
        addProcess(Priority.LOW);
        addProcess(Priority.LOW);
        addProcess(Priority.MEDIUM);
        addProcess(Priority.HIGH);

        //when
        Request request = Request.builder()
                .requestType(RequestType.KILL_ALL)
                .build();

        executeAndWait(request);

        //then
        assertEquals(0, processService.listAllProcesses().size());
    }
}
