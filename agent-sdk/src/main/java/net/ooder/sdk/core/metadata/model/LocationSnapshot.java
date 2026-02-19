
package net.ooder.sdk.core.metadata.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class LocationSnapshot implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private final String networkId;
    private final String host;
    private final int port;
    private final String endpoint;
    private final String region;
    private final String zone;
    private final String dataCenter;
    private final String networkType;
    private final double latitude;
    private final double longitude;
    private final Map<String, String> networkAttributes;
    private final long snapshotTime;
    
    public LocationSnapshot(LocationInfo location) {
        this.networkId = location.getNetworkId();
        this.host = location.getHost();
        this.port = location.getPort();
        this.endpoint = location.getEndpoint();
        this.region = location.getRegion();
        this.zone = location.getZone();
        this.dataCenter = location.getDataCenter();
        this.networkType = location.getNetworkType();
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
        this.networkAttributes = new HashMap<>(location.getNetworkAttributes());
        this.snapshotTime = System.currentTimeMillis();
    }
    
    public String getNetworkId() { return networkId; }
    public String getHost() { return host; }
    public int getPort() { return port; }
    public String getEndpoint() { return endpoint; }
    public String getRegion() { return region; }
    public String getZone() { return zone; }
    public String getDataCenter() { return dataCenter; }
    public String getNetworkType() { return networkType; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public Map<String, String> getNetworkAttributes() { return new HashMap<>(networkAttributes); }
    public long getSnapshotTime() { return snapshotTime; }
    
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
    
    public String getLocationKey() {
        return getAddress() != null ? getAddress() : "unknown";
    }
}
