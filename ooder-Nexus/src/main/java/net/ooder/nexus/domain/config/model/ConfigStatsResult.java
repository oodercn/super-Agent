package net.ooder.nexus.domain.config.model;

import java.io.Serializable;

/**
 * 配置统计结果
 * 用于ConfigController中getConfigStats方法的返回类型
 */
public class ConfigStatsResult implements Serializable {
    private int nexusConfigCount;
    private int routeConfigCount;
    private int endConfigCount;
    private int totalConfigCount;
    private int configChangeCount;
    private ConfigChange lastConfigChange;

    public int getNexusConfigCount() {
        return nexusConfigCount;
    }

    public void setNexusConfigCount(int nexusConfigCount) {
        this.nexusConfigCount = nexusConfigCount;
    }

    public int getRouteConfigCount() {
        return routeConfigCount;
    }

    public void setRouteConfigCount(int routeConfigCount) {
        this.routeConfigCount = routeConfigCount;
    }

    public int getEndConfigCount() {
        return endConfigCount;
    }

    public void setEndConfigCount(int endConfigCount) {
        this.endConfigCount = endConfigCount;
    }

    public int getTotalConfigCount() {
        return totalConfigCount;
    }

    public void setTotalConfigCount(int totalConfigCount) {
        this.totalConfigCount = totalConfigCount;
    }

    public int getConfigChangeCount() {
        return configChangeCount;
    }

    public void setConfigChangeCount(int configChangeCount) {
        this.configChangeCount = configChangeCount;
    }

    public ConfigChange getLastConfigChange() {
        return lastConfigChange;
    }

    public void setLastConfigChange(ConfigChange lastConfigChange) {
        this.lastConfigChange = lastConfigChange;
    }
}
