package net.ooder.nexus.skillcenter.dto.cloud;

import java.util.List;

public class CloudProviderInfoDTO {
    private String name;
    private List<String> regions;
    private List<String> instanceTypes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getRegions() {
        return regions;
    }

    public void setRegions(List<String> regions) {
        this.regions = regions;
    }

    public List<String> getInstanceTypes() {
        return instanceTypes;
    }

    public void setInstanceTypes(List<String> instanceTypes) {
        this.instanceTypes = instanceTypes;
    }
}
