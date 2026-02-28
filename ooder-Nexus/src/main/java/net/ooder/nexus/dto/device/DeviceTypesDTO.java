package net.ooder.nexus.dto.device;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class DeviceTypesDTO implements Serializable {
    private static final long serialVersionUID = 1L;

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
