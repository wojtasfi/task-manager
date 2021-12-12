package com.manager.infrastructure.process;

import lombok.experimental.UtilityClass;

import java.util.Comparator;

@UtilityClass
public class ComparingUtil {

    final Comparator<ProcessEntity> idComparator = Comparator.comparing(ProcessEntity::getPID);
    final Comparator<ProcessEntity> timeComparator = Comparator.comparing(ProcessEntity::getCreationDate);
    final Comparator<ProcessEntity> priorityComparator = Comparator.comparing(ProcessEntity::getPriority)
            .thenComparing(ProcessEntity::getPID);
}
