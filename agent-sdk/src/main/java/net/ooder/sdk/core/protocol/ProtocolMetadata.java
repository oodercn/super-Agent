package net.ooder.sdk.core.protocol;

import java.util.Map;

public class ProtocolMetadata {
    
    private String protocolType;
    private String version;
    private String encoding;
    private Map<String, String> properties;
    
    public String getProtocolType() { return protocolType; }
    public void setProtocolType(String protocolType) { this.protocolType = protocolType; }
    
    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
    
    public String getEncoding() { return encoding; }
    public void setEncoding(String encoding) { this.encoding = encoding; }
    
    public Map<String, String> getProperties() { return properties; }
    public void setProperties(Map<String, String> properties) { this.properties = properties; }
}
