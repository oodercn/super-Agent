package net.ooder.skillcenter.sdk.cloud;

import java.util.List;

public class NetworkConfig {
    private String vpcId;
    private String subnetId;
    private List<String> securityGroups;
    private boolean publicIp;
    private String dnsName;

    public String getVpcId() { return vpcId; }
    public void setVpcId(String vpcId) { this.vpcId = vpcId; }
    public String getSubnetId() { return subnetId; }
    public void setSubnetId(String subnetId) { this.subnetId = subnetId; }
    public List<String> getSecurityGroups() { return securityGroups; }
    public void setSecurityGroups(List<String> securityGroups) { this.securityGroups = securityGroups; }
    public boolean isPublicIp() { return publicIp; }
    public void setPublicIp(boolean publicIp) { this.publicIp = publicIp; }
    public String getDnsName() { return dnsName; }
    public void setDnsName(String dnsName) { this.dnsName = dnsName; }
}
