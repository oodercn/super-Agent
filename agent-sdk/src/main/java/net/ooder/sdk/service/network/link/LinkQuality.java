
package net.ooder.sdk.service.network.link;

public enum LinkQuality {
    
    GOOD("good", 0),
    FAIR("fair", 1),
    POOR("poor", 2);
    
    private final String code;
    private final int level;
    
    LinkQuality(String code, int level) {
        this.code = code;
        this.level = level;
    }
    
    public String getCode() { return code; }
    public int getLevel() { return level; }
}
