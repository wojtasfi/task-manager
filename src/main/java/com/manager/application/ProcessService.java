package com.manager.application;

import com.manager.application.strategies.AddProcessStrategy;
import com.manager.application.strategies.AddProcessStrategyName;
import com.manager.domain.Priority;
import com.manager.domain.Process;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class ProcessService {

    private static final Logger logger = LoggerFactory.getLogger(ProcessService.class);
    private static final Process emptyProcess = Process.of(null, null);

    private final Map<AddProcessStrategyName, AddProcessStrategy> addProcessStrategies;
    private final ProcessCoordinator processCoordinator;

    public Process addProcess(AddProcessStrategyName strategy, Priority priority) {

        try {
            return addProcessStrategies.get(strategy).addProcess(priority);
        } catch (RuntimeException exception) {
            logger.error("Not able to add new process", exception);
            return emptyProcess;
        }
    }

    public Void killProcess(Integer processId) {
        processCoordinator.kill(processId);
        return null;
    }

    public Void killGroup(Priority priority) {
        processCoordinator.killGroup(priority);
        return null;
    }

    public Void killAll() {
        processCoordinator.killAll();
        return null;
    }

    public List<Process> listAllProcesses() {
        return processCoordinator.getAllRunningProcesses();
    }

    public List<Process> listAllProcesses(ProcessSort processSort) {
        return processCoordinator.getAllRunningProcesses(processSort);
    }
}
