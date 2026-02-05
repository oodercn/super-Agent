package net.ooder.skillcenter.p2p.discovery;

import java.util.List;
import java.util.Map;

public interface SkillsAgentDiscoveryService {
    
    void startDiscovery();
    
    void stopDiscovery();
    
    void registerSkillsAgent(SkillsAgentInfo agentInfo);
    
    void unregisterSkillsAgent(String agentId);
    
    List<SkillsAgentInfo> discoverSkillsAgents();
    
    List<SkillsAgentInfo> discoverSkillsAgentsByCapability(String capability);
    
    void refreshSkillsAgent(String agentId);
    
    void refreshAllSkillsAgents();
    
    void addDiscoveryListener(SkillsAgentDiscoveryListener listener);
    
    void removeDiscoveryListener(SkillsAgentDiscoveryListener listener);
    
    class SkillsAgentInfo {
        private String agentId;
        private String agentName;
        private String agentType;
        private String ip;
        private int port;
        private List<String> capabilities;
        private Map<String, Object> metadata;
        private long lastSeen;
        private String status;
        
        public String getAgentId() {
            return agentId;
        }
        
        public void setAgentId(String agentId) {
            this.agentId = agentId;
        }
        
        public String getAgentName() {
            return agentName;
        }
        
        public void setAgentName(String agentName) {
            this.agentName = agentName;
        }
        
        public String getAgentType() {
            return agentType;
        }
        
        public void setAgentType(String agentType) {
            this.agentType = agentType;
        }
        
        public String getIp() {
            return ip;
        }
        
        public void setIp(String ip) {
            this.ip = ip;
        }
        
        public int getPort() {
            return port;
        }
        
        public void setPort(int port) {
            this.port = port;
        }
        
        public List<String> getCapabilities() {
            return capabilities;
        }
        
        public void setCapabilities(List<String> capabilities) {
            this.capabilities = capabilities;
        }
        
        public Map<String, Object> getMetadata() {
            return metadata;
        }
        
        public void setMetadata(Map<String, Object> metadata) {
            this.metadata = metadata;
        }
        
        public long getLastSeen() {
            return lastSeen;
        }
        
        public void setLastSeen(long lastSeen) {
            this.lastSeen = lastSeen;
        }
        
        public String getStatus() {
            return status;
        }
        
        public void setStatus(String status) {
            this.status = status;
        }
    }
    
    interface SkillsAgentDiscoveryListener {
        void onAgentDiscovered(SkillsAgentInfo agent);
        void onAgentLost(SkillsAgentInfo agent);
        void onAgentUpdated(SkillsAgentInfo agent);
    }
}
