package net.ooder.sdk.topology.model;

public enum LinkType {
    ETHERNET("ethernet", "以太网"),
    WIFI("wifi", "无线局域网"),
    BLUETOOTH("bluetooth", "蓝牙"),
    ZIGBEE("zigbee", " ZigBee"),
    CELLULAR("cellular", "蜂窝网络"),
    OPTICAL("optical", "光纤"),
    WIRELESS("wireless", "无线"),
    WIRED("wired", "有线"),
    VPN("vpn", "虚拟专用网络"),
    LOOPBACK("loopback", "回环"),
    UNKNOWN("unknown", "未知");
    
    private final String value;
    private final String description;
    
    LinkType(String value, String description) {
        this.value = value;
        this.description = description;
    }
    
    public String getValue() {
        return value;
    }
    
    public String getDescription() {
        return description;
    }
    
    @Override
    public String toString() {
        return value;
    }
}
