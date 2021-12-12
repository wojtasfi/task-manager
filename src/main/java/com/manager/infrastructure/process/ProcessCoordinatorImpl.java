package com.manager.infrastructure.process;

import com.manager.application.ProcessCoordinator;
import com.manager.application.ProcessSort;
import com.manager.domain.Priority;
import com.manager.domain.Process;
import com.manager.infrastructure.ClockProvider;
import com.manager.infrastructure.IdGenerator;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static com.manager.infrastructure.process.ComparingUtil.*;

@RequiredArgsConstructor
public class ProcessCoordinatorImpl implements ProcessCoordinator {

    private final List<ProcessEntity> runningProcesses = new LinkedList<>();
    private final Integer processListSize;
    private final IdGenerator idGenerator;
    private final ClockProvider clockProvider;

    @Override
    public List<Process> getAllRunningProcesses() {
        return runningProcesses.stream().map(ProcessEntity::toProcess).collect(Collectors.toList());
    }

    @Override
    public List<Process> getAllRunningProcesses(ProcessSort processSort) {
        return runningProcesses.stream()
                .sorted(getComparator(processSort))
                .map(ProcessEntity::toProcess)
                .collect(Collectors.toList());
    }

    @Override
    public Process addProcess(Priority priority) {
        Integer id = idGenerator.getNextId();
        ProcessEntity processEntity = ProcessEntity.of(id, priority, LocalDateTime.now(clockProvider.getClock()));
        this.runningProcesses.add(processEntity);
        return processEntity.toProcess();
    }

    @Override
    public void kill(Integer processId) {
        this.runningProcesses.stream()
                .filter(process -> process.getPID().equals(processId))
                .findFirst()
                .orElseThrow(() -> new ProcessNotFoundException(processId))
                .kill();
        refreshProcessList();
    }

    @Override
    public void killGroup(Priority priority) {
        this.runningProcesses.stream()
                .filter(process -> process.getPriority().equals(priority))
                .forEach(ProcessEntity::kill);
        refreshProcessList();
    }

    @Override
    public void killAll() {
        this.runningProcesses.forEach(ProcessEntity::kill);
        refreshProcessList();
        this.idGenerator.reset();
    }

    @Override
    public boolean capacityReached() {
        return this.runningProcesses.size() >= processListSize;
    }

    private void refreshProcessList() {
        List<ProcessEntity> killedProcesses = this.runningProcesses.stream()
                .filter(ProcessEntity::isKilled)
                .collect(Collectors.toList());

        this.runningProcesses.removeAll(killedProcesses);
    }

    private Comparator<ProcessEntity> getComparator(ProcessSort processSort) {
        return switch (processSort) {
            case ID -> idComparator;
            case TIME -> timeComparator;
            case PRIORITY -> priorityComparator;
        };
    }
}
