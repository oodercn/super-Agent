package net.ooder.nexus.model.system;

import java.io.Serializable;

public class CommandStatsData implements Serializable {
    private static final long serialVersionUID = 1L;

    private int totalCommands;
    private int successfulCommands;
    private int failedCommands;

    public CommandStatsData() {
    }

    public CommandStatsData(int totalCommands, int successfulCommands, int failedCommands) {
        this.totalCommands = totalCommands;
        this.successfulCommands = successfulCommands;
        this.failedCommands = failedCommands;
    }

    public int getTotalCommands() {
        return totalCommands;
    }

    public void setTotalCommands(int totalCommands) {
        this.totalCommands = totalCommands;
    }

    public int getSuccessfulCommands() {
        return successfulCommands;
    }

    public void setSuccessfulCommands(int successfulCommands) {
        this.successfulCommands = successfulCommands;
    }

    public int getFailedCommands() {
        return failedCommands;
    }

    public void setFailedCommands(int failedCommands) {
        this.failedCommands = failedCommands;
    }
}
