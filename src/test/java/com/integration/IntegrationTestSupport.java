package com.integration;

import com.manager.application.ProcessService;
import com.manager.application.strategies.AddProcessStrategyName;
import com.manager.domain.Priority;
import com.manager.domain.Process;
import com.manager.infrastructure.ClockProvider;
import com.manager.infrastructure.Context;
import com.manager.infrastructure.Request;
import com.manager.infrastructure.RequestHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class IntegrationTestSupport {

    protected RequestHandler requestHandler;
    protected ProcessService processService;
    protected TestClockProvider clockProvider;
    protected LocalDateTime defaultTime = LocalDateTime.of(2020, 1, 1, 1, 1, 1, 1);

    @BeforeEach
    void setUp() {
        this.clockProvider = new TestClockProvider(defaultTime);
        Context.setCustomNumberOfProcesses(4);
        Context.setClockProvider(clockProvider);
        Context context = Context.retrieveContext();
        processService = context.getProcessService();
        requestHandler = context.getRequestHandler();
    }

    @AfterEach
    void cleanUp() {
        Context.destroy();
    }

    protected Object executeAndWait(Request request) {
        CompletableFuture<?> future = requestHandler.executeRequest(request);
        CompletableFuture.allOf(future);
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    protected List<Process> addProcess(Priority priority, Integer numberOfProcesses) {
        return IntStream.range(0, numberOfProcesses)
                .mapToObj(x -> processService.addProcess(AddProcessStrategyName.DEFAULT, priority))
                .collect(Collectors.toList());
    }

    protected Process addProcess(Priority priority) {
        return processService.addProcess(AddProcessStrategyName.DEFAULT, priority);
    }

    protected boolean processExists(Integer processId) {
        return processService.listAllProcesses().stream().anyMatch(process -> process.getPID().equals(processId));
    }
}
