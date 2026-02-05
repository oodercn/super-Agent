package net.ooder.sdk.system.sleep;

import java.util.List;

public interface SleepStrategy {
    IdleSleepConfig getIdleSleep();
    ErrorSleepConfig getErrorSleep();
    MaintenanceSleepConfig getMaintenanceSleep();

    interface IdleSleepConfig {
        boolean isEnabled();
        long getCheckInterval();
        long getIdleTimeout();
    }

    interface ErrorSleepConfig {
        boolean isEnabled();
        long getSleepDuration();
        int getErrorThreshold();
    }

    interface MaintenanceSleepConfig {
        boolean isEnabled();
        List<String> getMaintenanceWindow();
    }
}
