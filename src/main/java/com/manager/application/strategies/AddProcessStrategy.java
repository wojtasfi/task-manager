package com.manager.application.strategies;

import com.manager.application.ProcessCoordinator;
import com.manager.domain.Priority;
import com.manager.domain.Process;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AddProcessStrategy {
    protected final ProcessCoordinator processCoordinator;

    public abstract Process addProcess(Priority priority);
}
