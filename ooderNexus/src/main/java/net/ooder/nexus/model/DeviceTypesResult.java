package net.ooder.nexus.model;

import java.util.List;
import java.util.Map;

/**
 * 设备类型结果实体Bean
 * 用于DeviceController中getDeviceTypes方法的返回类型
 */
public class DeviceTypesResult {
    
    private List<String> types;
    private Map<String, Long> typeCounts;

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public Map<String, Long> getTypeCounts() {
        return typeCounts;
    }

    public void setTypeCounts(Map<String, Long> typeCounts) {
        this.typeCounts = typeCounts;
    }
}
