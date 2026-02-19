
package net.ooder.sdk.core.metadata.model;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

public class LocationInfo implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String networkId;
    private String host;
    private int port;
    private String endpoint;
    private String region;
    private String zone;
    private String dataCenter;
    private String networkType;
    private double latitude;
    private double longitude;
    private Map<String, String> networkAttributes;
    
    public LocationInfo() {
        this.networkAttributes = new HashMap<>();
        this.networkType = "UDP";
    }
    
    public String getNetworkId() { return networkId; }
    public void setNetworkId(String networkId) { this.networkId = networkId; }
    
    public String getHost() { return host; }
    public void setHost(String host) { this.host = host; }
    
    public int getPort() { return port; }
    public void setPort(int port) { this.port = port; }
    
    public String getEndpoint() { return endpoint; }
    public void setEndpoint(String endpoint) { this.endpoint = endpoint; }
    
    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }
    
    public String getZone() { return zone; }
    public void setZone(String zone) { this.zone = zone; }
    
    public String getDataCenter() { return dataCenter; }
    public void setDataCenter(String dataCenter) { this.dataCenter = dataCenter; }
    
    public String getNetworkType() { return networkType; }
    public void setNetworkType(String networkType) { this.networkType = networkType; }
    
    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    
    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
    
    public Map<String, String> getNetworkAttributes() { return networkAttributes; }
    public void setNetworkAttributes(Map<String, String> networkAttributes) { 
        this.networkAttributes = networkAttributes != null ? networkAttributes : new HashMap<>(); 
    }
    
    public void setNetworkAttribute(String key, String value) { networkAttributes.put(key, value); }
    public String getNetworkAttribute(String key) { return networkAttributes.get(key); }
    
    public String getAddress() {
        if (endpoint != null && !endpoint.isEmpty()) {
            return endpoint;
        }
        if (host != null && !host.isEmpty()) {
            if (port > 0) {
                return host + ":" + port;
            }
            return host;
        }
        return null;
    }
    
    public String getFullAddress() {
        StringBuilder sb = new StringBuilder();
        if (networkType != null) sb.append(networkType.toLowerCase()).append("://");
        sb.append(getAddress() != null ? getAddress() : "unknown");
        return sb.toString();
    }
    
    public boolean hasValidAddress() {
        return host != null && !host.isEmpty() && port > 0;
    }
    
    public boolean isLocal() {
        if (host == null) return false;
        try {
            InetAddress addr = InetAddress.getByName(host);
            return addr.isLoopbackAddress() || addr.isSiteLocalAddress();
        } catch (UnknownHostException e) {
            return "localhost".equalsIgnoreCase(host) || "127.0.0.1".equals(host);
        }
    }
    
    public boolean isInSameRegion(LocationInfo other) {
        if (region == null || other == null) return false;
        return region.equals(other.getRegion());
    }
    
    public boolean isInSameZone(LocationInfo other) {
        if (zone == null || other == null) return false;
        return zone.equals(other.getZone());
    }
    
    public double distanceTo(LocationInfo other) {
        if (other == null) return -1;
        double lat1 = Math.toRadians(this.latitude);
        double lon1 = Math.toRadians(this.longitude);
        double lat2 = Math.toRadians(other.latitude);
        double lon2 = Math.toRadians(other.longitude);
        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;
        double a = Math.pow(Math.sin(dlat / 2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dlon / 2), 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return 6371 * c;
    }
    
    public String getLocationKey() {
        return getAddress() != null ? getAddress() : "unknown";
    }
    
    public LocationSnapshot snapshot() {
        return new LocationSnapshot(this);
    }
    
    public static LocationInfo of(String host, int port) {
        LocationInfo location = new LocationInfo();
        location.setHost(host);
        location.setPort(port);
        return location;
    }
    
    public static LocationInfo ofEndpoint(String endpoint) {
        LocationInfo location = new LocationInfo();
        location.setEndpoint(endpoint);
        if (endpoint != null && endpoint.contains(":")) {
            String[] parts = endpoint.split(":");
            if (parts.length == 2) {
                location.setHost(parts[0]);
                try {
                    location.setPort(Integer.parseInt(parts[1]));
                } catch (NumberFormatException ignored) {}
            }
        }
        return location;
    }
    
    public static LocationInfo local(int port) {
        LocationInfo location = new LocationInfo();
        location.setHost("0.0.0.0");
        location.setPort(port);
        location.setRegion("local");
        return location;
    }
}
