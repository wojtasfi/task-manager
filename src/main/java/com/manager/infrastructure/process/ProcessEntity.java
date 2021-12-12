package com.manager.infrastructure.process;

import com.manager.domain.Priority;
import com.manager.domain.Process;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;


@Getter
@EqualsAndHashCode
@RequiredArgsConstructor(staticName = "of")
public class ProcessEntity {
    private final Integer PID;
    private final Priority priority;
    private final LocalDateTime creationDate;
    private boolean killed;

    void kill() {
        this.killed = true;
    }

    Process toProcess() {
        return Process.of(PID, priority);
    }

}
