package net.ooder.sdk.system.sleep;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.annotation.JSONField;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class SleepManager {
    private static final Logger log = LoggerFactory.getLogger(SleepManager.class);
    private final SleepStrategy strategy;
    private final ScheduledExecutorService scheduler;
    private final AtomicBoolean isSleeping = new AtomicBoolean(false);
    private final AtomicLong lastActivityTime = new AtomicLong(System.currentTimeMillis());
    private final AtomicLong errorCount = new AtomicLong(0);
    private SleepMode currentMode = SleepMode.NONE;
    private WakeCallback wakeCallback;

    public enum SleepMode {
        @JSONField(ordinal = 0) NONE,
        @JSONField(ordinal = 1) IDLE,
        @JSONField(ordinal = 2) ERROR,
        @JSONField(ordinal = 3) POWER_SAVE,
        @JSONField(ordinal = 4) MAINTENANCE,
        @JSONField(ordinal = 5) FORCED
    }

    public interface WakeCallback {
        void onWake(SleepMode previousMode, String reason);
    }

    public SleepManager(SleepStrategy strategy) {
        this.strategy = strategy;
        this.scheduler = Executors.newScheduledThreadPool(1, r -> {
            Thread thread = new Thread(r, "sleep-manager");
            thread.setDaemon(true);
            return thread;
        });
    }

    public void start() {
        if (strategy.getIdleSleep() != null && strategy.getIdleSleep().isEnabled()) {
            scheduleIdleCheck();
        }
        if (strategy.getErrorSleep() != null && strategy.getErrorSleep().isEnabled()) {
            scheduleErrorCheck();
        }
        if (strategy.getMaintenanceSleep() != null && strategy.getMaintenanceSleep().isEnabled()) {
            scheduleMaintenanceCheck();
        }
    }

    public void stop() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    public void recordActivity() {
        lastActivityTime.set(System.currentTimeMillis());
        if (isSleeping.get() && currentMode == SleepMode.IDLE) {
            wakeUp(SleepMode.IDLE, "Activity detected");
        }
    }

    public void recordError() {
        errorCount.incrementAndGet();
    }

    public void resetErrorCount() {
        errorCount.set(0);
    }

    public void forceSleep(SleepMode mode) {
        if (!isSleeping.compareAndSet(false, true)) {
            log.warn("Already sleeping, cannot force sleep to {}", mode);
            return;
        }
        currentMode = mode;
        log.info("Entering forced sleep mode: {}", mode);
    }

    public void wakeUp(String reason) {
        wakeUp(currentMode, reason);
    }

    private void wakeUp(SleepMode previousMode, String reason) {
        if (!isSleeping.compareAndSet(true, false)) {
            return;
        }
        log.info("Waking up from {} mode, reason: {}", previousMode, reason);
        SleepMode oldMode = currentMode;
        currentMode = SleepMode.NONE;
        resetErrorCount();

        if (wakeCallback != null) {
            wakeCallback.onWake(oldMode, reason);
        }
    }

    public boolean canAcceptCommands() {
        return !isSleeping.get() || (currentMode == SleepMode.IDLE && strategy.getIdleSleep().isEnabled());
    }

    private void scheduleIdleCheck() {
        long interval = strategy.getIdleSleep().getCheckInterval();
        scheduler.scheduleAtFixedRate(() -> {
            long idleTime = System.currentTimeMillis() - lastActivityTime.get();
            if (idleTime >= strategy.getIdleSleep().getIdleTimeout() && !isSleeping.get()) {
                enterIdleSleep();
            }
        }, interval, interval, TimeUnit.MILLISECONDS);
    }

    private void scheduleErrorCheck() {
        long interval = strategy.getErrorSleep().getSleepDuration();
        scheduler.scheduleAtFixedRate(() -> {
            if (errorCount.get() >= strategy.getErrorSleep().getErrorThreshold() && !isSleeping.get()) {
                enterErrorSleep();
            }
        }, interval, interval, TimeUnit.MILLISECONDS);
    }

    private void scheduleMaintenanceCheck() {
        scheduler.scheduleAtFixedRate(() -> {
            if (isInMaintenanceWindow() && !isSleeping.get()) {
                enterMaintenanceSleep();
            } else if (!isInMaintenanceWindow() && isSleeping.get() && currentMode == SleepMode.MAINTENANCE) {
                wakeUp("Maintenance window ended");
            }
        }, 60, 60, TimeUnit.SECONDS);
    }

    private void enterIdleSleep() {
        if (isSleeping.compareAndSet(false, true)) {
            currentMode = SleepMode.IDLE;
            log.info("Entering idle sleep mode");
        }
    }

    private void enterErrorSleep() {
        if (isSleeping.compareAndSet(false, true)) {
            currentMode = SleepMode.ERROR;
            log.info("Entering error sleep mode after {} errors", errorCount.get());
        }
    }

    private boolean isInMaintenanceWindow() {
        if (strategy.getMaintenanceSleep() == null || !strategy.getMaintenanceSleep().isEnabled()) {
            return false;
        }
        return strategy.getMaintenanceSleep().getMaintenanceWindow().stream()
                .anyMatch(window -> isInWindow(window));
    }

    private boolean isInWindow(String window) {
        try {
            String[] parts = window.split("-");
            String start = parts[0];
            String end = parts[1];
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void enterMaintenanceSleep() {
        if (isSleeping.compareAndSet(false, true)) {
            currentMode = SleepMode.MAINTENANCE;
            log.info("Entering maintenance sleep mode");
        }
    }

    public void setWakeCallback(WakeCallback callback) {
        this.wakeCallback = callback;
    }

    public SleepStrategy getStrategy() {
        return strategy;
    }

    public boolean isSleeping() {
        return isSleeping.get();
    }

    public long getLastActivityTime() {
        return lastActivityTime.get();
    }

    public long getErrorCount() {
        return errorCount.get();
    }

    public SleepMode getCurrentMode() {
        return currentMode;
    }

    /**
     * 重置睡眠管理器状态
     */
    public void reset() {
        lastActivityTime.set(System.currentTimeMillis());
        resetErrorCount();
        
        // 如果当前正在睡眠，唤醒它
        if (isSleeping.get()) {
            wakeUp("Reset command received");
        }
        
        log.info("Sleep manager reset");
    }

    public WakeCallback getWakeCallback() {
        return wakeCallback;
    }
}
