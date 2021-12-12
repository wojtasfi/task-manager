package com.manager.infrastructure;

import com.manager.application.ProcessSort;
import com.manager.application.strategies.AddProcessStrategyName;
import com.manager.domain.Priority;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Request {
    private RequestType requestType;
    private AddProcessStrategyName addProcessStrategy;
    private Priority priority;
    private Integer processId;
    private ProcessSort processSort;
}
