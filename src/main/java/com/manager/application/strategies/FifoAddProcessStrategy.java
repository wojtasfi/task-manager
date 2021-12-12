package com.manager.application.strategies;

import com.manager.application.ProcessCoordinator;
import com.manager.domain.Priority;
import com.manager.domain.Process;

import java.util.List;

public class FifoAddProcessStrategy extends AddProcessStrategy {

    public FifoAddProcessStrategy(ProcessCoordinator processCoordinator) {
        super(processCoordinator);
    }

    @Override
    public Process addProcess(Priority priority) {

        if (processCoordinator.capacityReached()) {
            List<Process> processes = processCoordinator.getAllRunningProcesses();
            Process processToRemove = processes.get(processes.size() - 1);
            processCoordinator.kill(processToRemove.getPID());
        }
        return processCoordinator.addProcess(priority);
    }
}
