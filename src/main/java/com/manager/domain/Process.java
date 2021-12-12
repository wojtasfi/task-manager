package com.manager.domain;

import lombok.Value;

@Value(staticConstructor = "of")
public class Process {

    Integer PID;
    Priority priority;


}
