package net.ooder.mvp.skill.scene.capability.model;

import java.io.Serializable;

public class CapabilityAddress implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String address;
    private String name;
    private String fallback;
    private String description;
    private boolean required;
    private boolean skipable;
    
    public CapabilityAddress() {}
    
    public CapabilityAddress(String address, String name, String fallback) {
        this.address = address;
        this.name = name;
        this.fallback = fallback;
        this.required = true;
    }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getFallback() { return fallback; }
    public void setFallback(String fallback) { this.fallback = fallback; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public boolean isRequired() { return required; }
    public void setRequired(boolean required) { this.required = required; }
    
    public boolean isSkipable() { return skipable; }
    public void setSkipable(boolean skipable) { this.skipable = skipable; }
    
    public int getAddressValue() {
        if (address == null) return 0;
        if (address.startsWith("0x")) {
            return Integer.parseInt(address.substring(2), 16);
        }
        return Integer.parseInt(address);
    }
    
    public CapabilityCategory getCategory() {
        return CapabilityCategory.fromAddress(getAddressValue());
    }
}
