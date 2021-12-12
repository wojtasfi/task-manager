package com.integration;

import com.manager.application.ProcessSort;
import com.manager.domain.Priority;
import com.manager.domain.Process;
import com.manager.infrastructure.Request;
import com.manager.infrastructure.RequestType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

public class ProcessServiceListProcessesIT extends IntegrationTestSupport{

    @Test
    void should_list_processes_sorted_by_id() {
        //given
        Process first = addProcess(Priority.MEDIUM);
        Process second = addProcess(Priority.LOW);
        Process third =  addProcess(Priority.HIGH);

        //when
        Request request = Request.builder()
                .requestType(RequestType.LIST)
                .processSort(ProcessSort.ID)
                .build();

        List<Process> processList = (List<Process>) executeAndWait(request);

        //then
        Assertions.assertThat(processList)
                .hasSize(3)
                .extracting(Process::getPID)
                .containsExactly(first.getPID(), second.getPID(), third.getPID());
    }

    @Test
    void should_list_processes_sorted_by_priority() {
        //given
        Process medium = addProcess(Priority.MEDIUM);
        Process low = addProcess(Priority.LOW);
        Process high =  addProcess(Priority.HIGH);

        //when
        Request request = Request.builder()
                .requestType(RequestType.LIST)
                .processSort(ProcessSort.PRIORITY)
                .build();

        List<Process> processList = (List<Process>) executeAndWait(request);

        //then
        Assertions.assertThat(processList)
                .hasSize(3)
                .extracting(Process::getPID)
                .containsExactly(low.getPID(), medium.getPID(), high.getPID());
    }

    @Test
    void should_list_processes_sorted_by_creation_time() {
        //given
        LocalDateTime earliest = LocalDateTime.of(2020, 1, 1, 1, 1, 1, 1);
        LocalDateTime middle = LocalDateTime.of(2021, 1, 1, 1, 1, 1, 1);
        LocalDateTime latest = LocalDateTime.of(2022, 1, 1, 1, 1, 1, 1);

        clockProvider.overrideTime(middle);
        Process middleProcess = addProcess(Priority.HIGH);

        clockProvider.overrideTime(latest);
        Process latestProcess = addProcess(Priority.MEDIUM);

        clockProvider.overrideTime(earliest);
        Process earliestProcess = addProcess(Priority.MEDIUM);

        //when
        Request request = Request.builder()
                .requestType(RequestType.LIST)
                .processSort(ProcessSort.TIME)
                .build();

        List<Process> processList = (List<Process>) executeAndWait(request);

        //then
        Assertions.assertThat(processList)
                .hasSize(3)
                .extracting(Process::getPID)
                .containsExactly(earliestProcess.getPID(), middleProcess.getPID(), latestProcess.getPID());
    }
}
