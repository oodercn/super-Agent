
package net.ooder.sdk.common.enums;

public enum DiscoveryMethod {
    UDP_BROADCAST("udp_broadcast", "UDP Broadcast discovery"),
    DHT_KADEMLIA("dht_kademlia", "DHT/Kademlia discovery"),
    MDNS_DNS_SD("mdns_dns_sd", "mDNS/DNS-SD discovery"),
    SKILL_CENTER("skill_center", "SkillCenter API discovery"),
    LOCAL_FS("local_fs", "Local filesystem discovery"),
    GITHUB("github", "GitHub repository discovery"),
    GITEE("gitee", "Gitee repository discovery"),
    GIT_REPOSITORY("git_repository", "Git repository discovery (generic)"),
    AUTO("auto", "Auto detect discovery method");
    
    private final String code;
    private final String description;
    
    DiscoveryMethod(String code, String description) {
        this.code = code;
        this.description = description;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getDescription() {
        return description;
    }
    
    public static DiscoveryMethod fromCode(String code) {
        for (DiscoveryMethod method : values()) {
            if (method.code.equalsIgnoreCase(code)) {
                return method;
            }
        }
        throw new IllegalArgumentException("Unknown discovery method: " + code);
    }
    
    public static DiscoveryMethod inferFromSource(String source) {
        if (source == null || source.isEmpty()) {
            return LOCAL_FS;
        }
        switch (source.toLowerCase()) {
            case "github":
                return GITHUB;
            case "gitee":
                return GITEE;
            case "skill_center":
            case "skillcenter":
                return SKILL_CENTER;
            case "local":
            case "local_fs":
                return LOCAL_FS;
            default:
                return LOCAL_FS;
        }
    }
}
