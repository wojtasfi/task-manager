package com.manager.infrastructure;

import com.manager.application.ProcessCoordinator;
import com.manager.application.ProcessService;
import com.manager.application.strategies.AddProcessStrategyName;
import com.manager.application.strategies.DefaultAddProcessStrategy;
import com.manager.application.strategies.FifoAddProcessStrategy;
import com.manager.application.strategies.PriorityAddProcessStrategy;
import com.manager.application.strategies.AddProcessStrategy;
import com.manager.infrastructure.process.ProcessCoordinatorImpl;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class Context {

    private static final Integer defaultNumberOfProcesses = 20;
    private static Integer customNumberOfProcesses;
    private static ClockProvider appClockProvider;
    private static Context instance;

    @Getter
    public final ProcessService processService;
    @Getter
    public final RequestHandler requestHandler;

    private Context(ProcessService processService, RequestHandler requestHandler) {
        this.processService = processService;
        this.requestHandler = requestHandler;
    }

    public static void setCustomNumberOfProcesses(Integer nrOfProcesses) {
        if (instance == null) {
            customNumberOfProcesses = nrOfProcesses;
        } else {
            throw new RuntimeException("Context already exists! Destroy context before changing number of processes.");
        }
    }

    public static void setClockProvider(ClockProvider clockProvider) {
        if (instance == null) {
            appClockProvider = clockProvider;
        } else {
            throw new RuntimeException("Context already exists! Destroy context before setting clock.");
        }
    }

    public static Context retrieveContext() {
        if (instance != null) {
            return instance;
        }

        if (customNumberOfProcesses == null) {
            instance = createContext(defaultNumberOfProcesses);
        } else {
            instance = createContext(customNumberOfProcesses);
        }

        return instance;
    }

    public static void destroy() {
        instance = null;
    }

    private static Context createContext(Integer processListSize) {

        ProcessCoordinator processCoordinator = new ProcessCoordinatorImpl(processListSize, new IdGenerator(), appClockProvider);

        DefaultAddProcessStrategy defaultAddProcessStrategy = new DefaultAddProcessStrategy(processCoordinator);
        FifoAddProcessStrategy fifoAddProcessStrategy = new FifoAddProcessStrategy(processCoordinator);
        PriorityAddProcessStrategy priorityAddProcessStrategy = new PriorityAddProcessStrategy(processCoordinator);

        Map<AddProcessStrategyName, AddProcessStrategy> addProcessStrategies = new HashMap<>();
        addProcessStrategies.put(AddProcessStrategyName.DEFAULT, defaultAddProcessStrategy);
        addProcessStrategies.put(AddProcessStrategyName.FIFO, fifoAddProcessStrategy);
        addProcessStrategies.put(AddProcessStrategyName.PRIORITY, priorityAddProcessStrategy);

        ProcessService processService = new ProcessService(addProcessStrategies, processCoordinator);
        RequestHandler requestHandler = new RequestHandler(processService);

        return new Context(processService, requestHandler);
    }
}
