package net.ooder.skillcenter.resources.model;

/**
 * 资源请求 - 符合v0.7.0协议规范
 */
public class ResourceRequest {
    
    private String cpu;
    private String memory;
    private String storage;
    private NetworkPolicy network;
    
    public ResourceRequest() {}
    
    public static ResourceRequest of(String cpu, String memory, String storage) {
        ResourceRequest request = new ResourceRequest();
        request.setCpu(cpu);
        request.setMemory(memory);
        request.setStorage(storage);
        return request;
    }
    
    public static ResourceRequest minimal() {
        return of("100m", "128Mi", "256Mi");
    }
    
    public static ResourceRequest standard() {
        return of("500m", "512Mi", "1Gi");
    }
    
    public static ResourceRequest high() {
        return of("1000m", "1Gi", "2Gi");
    }
    
    public long getCpuMillis() {
        if (cpu == null) return 0;
        if (cpu.endsWith("m")) {
            return Long.parseLong(cpu.substring(0, cpu.length() - 1));
        }
        return Long.parseLong(cpu) * 1000;
    }
    
    public long getMemoryBytes() {
        return parseSize(memory);
    }
    
    public long getStorageBytes() {
        return parseSize(storage);
    }
    
    private long parseSize(String size) {
        if (size == null) return 0;
        size = size.toUpperCase();
        try {
            if (size.endsWith("GI")) {
                return Long.parseLong(size.substring(0, size.length() - 2)) * 1024 * 1024 * 1024;
            } else if (size.endsWith("MI")) {
                return Long.parseLong(size.substring(0, size.length() - 2)) * 1024 * 1024;
            } else if (size.endsWith("KI")) {
                return Long.parseLong(size.substring(0, size.length() - 2)) * 1024;
            } else if (size.endsWith("G")) {
                return Long.parseLong(size.substring(0, size.length() - 1)) * 1024 * 1024 * 1024;
            } else if (size.endsWith("M")) {
                return Long.parseLong(size.substring(0, size.length() - 1)) * 1024 * 1024;
            } else if (size.endsWith("K")) {
                return Long.parseLong(size.substring(0, size.length() - 1)) * 1024;
            }
            return Long.parseLong(size);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    
    public String getCpu() { return cpu; }
    public void setCpu(String cpu) { this.cpu = cpu; }
    
    public String getMemory() { return memory; }
    public void setMemory(String memory) { this.memory = memory; }
    
    public String getStorage() { return storage; }
    public void setStorage(String storage) { this.storage = storage; }
    
    public NetworkPolicy getNetwork() { return network; }
    public void setNetwork(NetworkPolicy network) { this.network = network; }
}
