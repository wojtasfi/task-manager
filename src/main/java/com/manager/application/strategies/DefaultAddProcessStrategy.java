package com.manager.application.strategies;

import com.manager.application.ProcessCoordinator;
import com.manager.application.strategies.exceptions.CapacityReachedException;
import com.manager.domain.Priority;
import com.manager.domain.Process;

public class DefaultAddProcessStrategy extends AddProcessStrategy {

    public DefaultAddProcessStrategy(ProcessCoordinator processCoordinator) {
        super(processCoordinator);
    }

    @Override
    public Process addProcess(Priority priority) {

        if(processCoordinator.capacityReached()) {
            throw new CapacityReachedException();
        }
        return processCoordinator.addProcess(priority);
    }
}
