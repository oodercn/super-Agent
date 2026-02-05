package net.ooder.sdk.system.sleep;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SleepStrategyTest {

    @Test
    void testDefaultSleepStrategy() {
        DefaultSleepStrategy strategy = new DefaultSleepStrategy();

        // 测试默认配置
        assertFalse(strategy.getIdleSleep().isEnabled());
        assertEquals(60000, strategy.getIdleSleep().getCheckInterval());
        assertEquals(3600000, strategy.getIdleSleep().getIdleTimeout());

        assertFalse(strategy.getErrorSleep().isEnabled());
        assertEquals(30000, strategy.getErrorSleep().getSleepDuration());
        assertEquals(5, strategy.getErrorSleep().getErrorThreshold());

        assertFalse(strategy.getMaintenanceSleep().isEnabled());
        assertTrue(strategy.getMaintenanceSleep().getMaintenanceWindow().isEmpty());
    }

    @Test
    void testSleepStrategyInterface() {
        SleepStrategy strategy = new DefaultSleepStrategy();

        // 测试IdleSleepConfig接口
        SleepStrategy.IdleSleepConfig idleConfig = strategy.getIdleSleep();
        assertNotNull(idleConfig);
        assertFalse(idleConfig.isEnabled());
        assertEquals(60000, idleConfig.getCheckInterval());
        assertEquals(3600000, idleConfig.getIdleTimeout());

        // 测试ErrorSleepConfig接口
        SleepStrategy.ErrorSleepConfig errorConfig = strategy.getErrorSleep();
        assertNotNull(errorConfig);
        assertFalse(errorConfig.isEnabled());
        assertEquals(30000, errorConfig.getSleepDuration());
        assertEquals(5, errorConfig.getErrorThreshold());

        // 测试MaintenanceSleepConfig接口
        SleepStrategy.MaintenanceSleepConfig maintenanceConfig = strategy.getMaintenanceSleep();
        assertNotNull(maintenanceConfig);
        assertFalse(maintenanceConfig.isEnabled());
        assertTrue(maintenanceConfig.getMaintenanceWindow().isEmpty());
    }

    @Test
    void testSleepStrategyInstance() {
        DefaultSleepStrategy strategy = new DefaultSleepStrategy();

        // 测试所有配置都不为空
        assertNotNull(strategy.getIdleSleep());
        assertNotNull(strategy.getErrorSleep());
        assertNotNull(strategy.getMaintenanceSleep());

        // 测试默认配置值
        assertEquals(60000, strategy.getIdleSleep().getCheckInterval());
        assertEquals(3600000, strategy.getIdleSleep().getIdleTimeout());
        assertEquals(30000, strategy.getErrorSleep().getSleepDuration());
        assertEquals(5, strategy.getErrorSleep().getErrorThreshold());
        assertTrue(strategy.getMaintenanceSleep().getMaintenanceWindow().isEmpty());
    }
}
