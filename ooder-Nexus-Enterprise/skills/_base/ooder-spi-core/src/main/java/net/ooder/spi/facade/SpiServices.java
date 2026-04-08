package net.ooder.spi.facade;

import net.ooder.spi.im.ImService;
import net.ooder.spi.rag.RagEnhanceDriver;
import net.ooder.spi.workflow.WorkflowDriver;
import java.util.Optional;

public class SpiServices {

    private static SpiServices instance;

    private ImService imService;
    private RagEnhanceDriver ragEnhanceDriver;
    private WorkflowDriver workflowDriver;

    public static synchronized void init(SpiServices services) {
        instance = services;
    }

    public static SpiServices getInstance() {
        return instance;
    }

    public static ImService getImService() {
        return instance != null ? instance.imService : null;
    }

    public static RagEnhanceDriver getRagEnhanceDriver() {
        return instance != null ? instance.ragEnhanceDriver : null;
    }

    public static WorkflowDriver getWorkflowDriver() {
        return instance != null ? instance.workflowDriver : null;
    }

    public static Optional<ImService> im() { return Optional.ofNullable(getImService()); }
    public static Optional<RagEnhanceDriver> rag() { return Optional.ofNullable(getRagEnhanceDriver()); }
    public static Optional<WorkflowDriver> workflow() { return Optional.ofNullable(getWorkflowDriver()); }

    public void setImService(ImService imService) { this.imService = imService; }
    public void setRagEnhanceDriver(RagEnhanceDriver ragEnhanceDriver) { this.ragEnhanceDriver = ragEnhanceDriver; }
    public void setWorkflowDriver(WorkflowDriver workflowDriver) { this.workflowDriver = workflowDriver; }
}
