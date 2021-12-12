package com.manager.infrastructure;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.manager.application.ProcessService;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.CompletableFuture;

public class RequestHandler {

    private final ProcessService processService;
    private final ThreadPoolExecutor threadPoolExecutor;

    public RequestHandler(ProcessService processService) {
        this.processService = processService;

        ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNameFormat("TaskManagerPool-%d")
                .build();

        threadPoolExecutor = new ThreadPoolExecutor(
                20, 20, 0, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(20), threadFactory
        );
    }

    public CompletableFuture<?> executeRequest(Request request) {
        return switch (request.getRequestType()) {
            case ADD -> CompletableFuture.supplyAsync(
                    () -> processService.addProcess(request.getAddProcessStrategy(), request.getPriority()),
                    threadPoolExecutor
            );
            case LIST -> CompletableFuture.supplyAsync(
                    () -> processService.listAllProcesses(request.getProcessSort()), threadPoolExecutor
            );
            case KILL -> CompletableFuture.supplyAsync(
                    () -> processService.killProcess(request.getProcessId()), threadPoolExecutor
            );
            case KILL_GROUP -> CompletableFuture.supplyAsync(
                    () -> processService.killGroup(request.getPriority()), threadPoolExecutor
            );
            case KILL_ALL -> CompletableFuture.supplyAsync(processService::killAll, threadPoolExecutor);
        };
    }
}
