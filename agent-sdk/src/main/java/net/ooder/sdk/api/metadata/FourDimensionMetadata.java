
package net.ooder.sdk.api.metadata;

import net.ooder.sdk.core.metadata.model.AgentMetadata;
import net.ooder.sdk.core.metadata.model.IdentityInfo;
import net.ooder.sdk.core.metadata.model.LocationInfo;
import net.ooder.sdk.core.metadata.model.ResourceInfo;
import net.ooder.sdk.core.metadata.model.Timeline;
import net.ooder.sdk.core.metadata.model.Context;

public class FourDimensionMetadata {
    
    private String entityId;
    private String entityType;
    private final AgentMetadata metadata;
    private long timestamp;
    
    public FourDimensionMetadata() {
        this.metadata = new AgentMetadata();
        this.timestamp = System.currentTimeMillis();
    }
    
    public FourDimensionMetadata(AgentMetadata metadata) {
        this.metadata = metadata != null ? metadata : new AgentMetadata();
        this.timestamp = System.currentTimeMillis();
    }
    
    public String getEntityId() {
        return entityId != null ? entityId : metadata.getAgentId();
    }
    
    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }
    
    public String getEntityType() {
        return entityType != null ? entityType : metadata.getAgentType();
    }
    
    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }
    
    public IdentityInfo getIdentity() {
        return metadata.identity();
    }
    
    public LocationInfo getLocation() {
        return metadata.location();
    }
    
    public ResourceInfo getResource() {
        return metadata.resource();
    }
    
    public Timeline getHistory() {
        return metadata.history();
    }
    
    public void setIdentity(IdentityInfo identity) {
        if (identity != null) {
            metadata.identity().setAgentId(identity.getAgentId());
            metadata.identity().setAgentName(identity.getAgentName());
            metadata.identity().setAgentType(identity.getAgentType());
            metadata.identity().setOwnerId(identity.getOwnerId());
            metadata.identity().setOrganizationId(identity.getOrganizationId());
            metadata.identity().setSceneId(identity.getSceneId());
            metadata.identity().setSceneGroupId(identity.getSceneGroupId());
            metadata.identity().setSkillId(identity.getSkillId());
            metadata.identity().setRole(identity.getRole());
        }
    }
    
    public void setLocation(LocationInfo location) {
        if (location != null) {
            metadata.location().setHost(location.getHost());
            metadata.location().setPort(location.getPort());
            metadata.location().setEndpoint(location.getEndpoint());
            metadata.location().setRegion(location.getRegion());
            metadata.location().setZone(location.getZone());
            metadata.location().setDataCenter(location.getDataCenter());
            metadata.location().setNetworkType(location.getNetworkType());
        }
    }
    
    public void setResource(ResourceInfo resource) {
        if (resource != null) {
            metadata.resource().setResourceType(resource.getResourceType());
            metadata.resource().setResourceId(resource.getResourceId());
            metadata.resource().setResourceName(resource.getResourceName());
            metadata.resource().setVersion(resource.getVersion());
            metadata.resource().setStatus(resource.getStatus());
            metadata.resource().setCapabilities(resource.getCapabilities());
            metadata.resource().setDependencies(resource.getDependencies());
        }
    }
    
    public long getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    
    public AgentMetadata getMetadata() {
        return metadata;
    }
    
    public Context getCurrentContext() {
        return metadata.getCurrentContext();
    }
    
    public String getAgentId() { return metadata.getAgentId(); }
    public String getAgentName() { return metadata.getAgentName(); }
    public String getAgentType() { return metadata.getAgentType(); }
    public String getAddress() { return metadata.getAddress(); }
    public boolean isOnline() { return metadata.isOnline(); }
    public String getStatus() { return metadata.getStatus(); }
    public boolean hasCapability(String capability) { return metadata.hasCapability(capability); }
    
    public String getSummary() {
        return metadata.getSummary();
    }
    
    public String getFullReport() {
        return metadata.getFullReport();
    }
    
    public static FourDimensionMetadata from(AgentMetadata metadata) {
        FourDimensionMetadata fdm = new FourDimensionMetadata(metadata);
        fdm.setEntityId(metadata.getAgentId());
        fdm.setEntityType(metadata.getAgentType());
        return fdm;
    }
    
    public static FourDimensionMetadata of(String agentId, String agentName, String agentType) {
        FourDimensionMetadata fdm = new FourDimensionMetadata();
        fdm.setEntityId(agentId);
        fdm.setEntityType(agentType);
        fdm.getIdentity().setAgentId(agentId);
        fdm.getIdentity().setAgentName(agentName);
        fdm.getIdentity().setAgentType(agentType);
        return fdm;
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private final FourDimensionMetadata metadata = new FourDimensionMetadata();
        
        public Builder entityId(String entityId) {
            metadata.setEntityId(entityId);
            return this;
        }
        
        public Builder entityType(String entityType) {
            metadata.setEntityType(entityType);
            return this;
        }
        
        public Builder agentId(String agentId) {
            metadata.getIdentity().setAgentId(agentId);
            return this;
        }
        
        public Builder agentName(String agentName) {
            metadata.getIdentity().setAgentName(agentName);
            return this;
        }
        
        public Builder agentType(String agentType) {
            metadata.getIdentity().setAgentType(agentType);
            return this;
        }
        
        public Builder host(String host) {
            metadata.getLocation().setHost(host);
            return this;
        }
        
        public Builder port(int port) {
            metadata.getLocation().setPort(port);
            return this;
        }
        
        public Builder region(String region) {
            metadata.getLocation().setRegion(region);
            return this;
        }
        
        public Builder status(String status) {
            metadata.getResource().setStatus(status);
            return this;
        }
        
        public Builder capability(String capability) {
            metadata.getResource().addCapability(capability);
            return this;
        }
        
        public FourDimensionMetadata build() {
            return metadata;
        }
    }
}
