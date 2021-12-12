package com.manager.application.strategies;

import com.manager.application.ProcessCoordinator;
import com.manager.application.strategies.exceptions.NoProcessesWithLowerPriorityFoundException;
import com.manager.domain.Priority;
import com.manager.domain.Process;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PriorityAddProcessStrategy extends AddProcessStrategy {

    public PriorityAddProcessStrategy(ProcessCoordinator processCoordinator) {
        super(processCoordinator);
    }

    @Override
    public Process addProcess(Priority priority) {

        if (processCoordinator.capacityReached()) {
            removeOldestWithLowestPriority(priority);
        }
        return processCoordinator.addProcess(priority);
    }

    private void removeOldestWithLowestPriority(Priority newProcessPriority) {
        List<Process> processesWithLowestPriority = processCoordinator.getAllRunningProcesses()
                .stream()
                .filter(otherProcess -> otherProcess.getPriority().isLowerThan(newProcessPriority))
                .collect(Collectors.groupingBy(Process::getPriority))
                .entrySet()
                .stream()
                .min(this::byPriority)
                .orElseThrow(NoProcessesWithLowerPriorityFoundException::new)
                .getValue();

        Process oldestProcess = processesWithLowestPriority.get(0);
        processCoordinator.kill(oldestProcess.getPID());
    }

    private int byPriority(Map.Entry<Priority, List<Process>> entry, Map.Entry<Priority, List<Process>> otherEntry) {
        int priorityOrder = entry.getKey().getPriorityOrder();
        int otherPriorityOrder = otherEntry.getKey().getPriorityOrder();

        if (priorityOrder > otherPriorityOrder) {
            return 1;
        } else if (priorityOrder < otherPriorityOrder) {
            return -1;
        }
        return 0;
    }
}
