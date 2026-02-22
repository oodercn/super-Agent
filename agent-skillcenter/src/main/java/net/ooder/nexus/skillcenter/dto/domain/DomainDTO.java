package net.ooder.nexus.skillcenter.dto.domain;

import java.util.List;
import java.util.Map;

public class DomainDTO {
    private String domainId;
    private String name;
    private String description;
    private String status;
    private String domainType;
    private String ownerAgentId;
    private List<String> memberAgentIds;
    private DomainPolicyDTO policy;
    private DomainTopologyDTO topology;
    private long createdAt;
    private long updatedAt;

    public String getDomainId() { return domainId; }
    public void setDomainId(String domainId) { this.domainId = domainId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getDomainType() { return domainType; }
    public void setDomainType(String domainType) { this.domainType = domainType; }
    public String getOwnerAgentId() { return ownerAgentId; }
    public void setOwnerAgentId(String ownerAgentId) { this.ownerAgentId = ownerAgentId; }
    public List<String> getMemberAgentIds() { return memberAgentIds; }
    public void setMemberAgentIds(List<String> memberAgentIds) { this.memberAgentIds = memberAgentIds; }
    public DomainPolicyDTO getPolicy() { return policy; }
    public void setPolicy(DomainPolicyDTO policy) { this.policy = policy; }
    public DomainTopologyDTO getTopology() { return topology; }
    public void setTopology(DomainTopologyDTO topology) { this.topology = topology; }
    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
    public long getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(long updatedAt) { this.updatedAt = updatedAt; }
}
