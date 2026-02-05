package net.ooder.sdk.system.sleep;

import java.util.ArrayList;
import java.util.List;

public class DefaultSleepStrategy implements SleepStrategy {
    private IdleSleepConfig idleSleep;
    private ErrorSleepConfig errorSleep;
    private MaintenanceSleepConfig maintenanceSleep;

    public DefaultSleepStrategy() {
        this.idleSleep = new DefaultIdleSleepConfig(false, 60000, 3600000);
        this.errorSleep = new DefaultErrorSleepConfig(false, 30000, 5);
        this.maintenanceSleep = new DefaultMaintenanceSleepConfig(false, new ArrayList<>());
    }

    @Override
    public IdleSleepConfig getIdleSleep() {
        return idleSleep;
    }

    @Override
    public ErrorSleepConfig getErrorSleep() {
        return errorSleep;
    }

    @Override
    public MaintenanceSleepConfig getMaintenanceSleep() {
        return maintenanceSleep;
    }

    public void setIdleSleep(IdleSleepConfig idleSleep) {
        this.idleSleep = idleSleep;
    }

    public void setErrorSleep(ErrorSleepConfig errorSleep) {
        this.errorSleep = errorSleep;
    }

    public void setMaintenanceSleep(MaintenanceSleepConfig maintenanceSleep) {
        this.maintenanceSleep = maintenanceSleep;
    }

    private static class DefaultIdleSleepConfig implements IdleSleepConfig {
        private boolean enabled;
        private long checkInterval;
        private long idleTimeout;

        public DefaultIdleSleepConfig(boolean enabled, long checkInterval, long idleTimeout) {
            this.enabled = enabled;
            this.checkInterval = checkInterval;
            this.idleTimeout = idleTimeout;
        }

        @Override
        public boolean isEnabled() {
            return enabled;
        }

        @Override
        public long getCheckInterval() {
            return checkInterval;
        }

        @Override
        public long getIdleTimeout() {
            return idleTimeout;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public void setCheckInterval(long checkInterval) {
            this.checkInterval = checkInterval;
        }

        public void setIdleTimeout(long idleTimeout) {
            this.idleTimeout = idleTimeout;
        }
    }

    private static class DefaultErrorSleepConfig implements ErrorSleepConfig {
        private boolean enabled;
        private long sleepDuration;
        private int errorThreshold;

        public DefaultErrorSleepConfig(boolean enabled, long sleepDuration, int errorThreshold) {
            this.enabled = enabled;
            this.sleepDuration = sleepDuration;
            this.errorThreshold = errorThreshold;
        }

        @Override
        public boolean isEnabled() {
            return enabled;
        }

        @Override
        public long getSleepDuration() {
            return sleepDuration;
        }

        @Override
        public int getErrorThreshold() {
            return errorThreshold;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public void setSleepDuration(long sleepDuration) {
            this.sleepDuration = sleepDuration;
        }

        public void setErrorThreshold(int errorThreshold) {
            this.errorThreshold = errorThreshold;
        }
    }

    private static class DefaultMaintenanceSleepConfig implements MaintenanceSleepConfig {
        private boolean enabled;
        private List<String> maintenanceWindow;

        public DefaultMaintenanceSleepConfig(boolean enabled, List<String> maintenanceWindow) {
            this.enabled = enabled;
            this.maintenanceWindow = maintenanceWindow;
        }

        @Override
        public boolean isEnabled() {
            return enabled;
        }

        @Override
        public List<String> getMaintenanceWindow() {
            return maintenanceWindow;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public void setMaintenanceWindow(List<String> maintenanceWindow) {
            this.maintenanceWindow = maintenanceWindow;
        }
    }
}
