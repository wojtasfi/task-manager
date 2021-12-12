package com.manager.application;

import com.manager.domain.Priority;
import com.manager.domain.Process;

import java.util.List;

public interface ProcessCoordinator {
    List<Process> getAllRunningProcesses();

    List<Process> getAllRunningProcesses(ProcessSort processSort);

    Process addProcess(Priority priority);

    void kill(Integer processId);

    void killGroup(Priority priority);

    void killAll();

    boolean capacityReached();
}
