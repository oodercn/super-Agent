/**
 * ENexus API - 企业级管理 API
 */
const ENexusAPI = {
    
    baseUrl: '/api/enexus',
    
    async request(url, data = {}) {
        const response = await fetch(url, {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(data)
        });
        return await response.json();
    },
    
    // MCP Agent 管理
    mcp: {
        async list(params = {}) {
            return ENexusAPI.request(ENexusAPI.baseUrl + '/mcp/list', params);
        },
        
        async get(mcpId) {
            return ENexusAPI.request(ENexusAPI.baseUrl + '/mcp/get', {mcpId});
        },
        
        async register(data) {
            return ENexusAPI.request(ENexusAPI.baseUrl + '/mcp/register', data);
        },
        
        async approve(mcpId) {
            return ENexusAPI.request(ENexusAPI.baseUrl + '/mcp/approve', {mcpId});
        },
        
        async toggle(mcpId) {
            return ENexusAPI.request(ENexusAPI.baseUrl + '/mcp/toggle', {mcpId});
        },
        
        async delete(mcpId) {
            return ENexusAPI.request(ENexusAPI.baseUrl + '/mcp/delete', {mcpId});
        },
        
        async heartbeatStats() {
            return ENexusAPI.request(ENexusAPI.baseUrl + '/mcp/heartbeat/stats', {});
        }
    },
    
    // Nexus 实例管理
    nexus: {
        async list(params = {}) {
            return ENexusAPI.request(ENexusAPI.baseUrl + '/nexus/list', params);
        },
        
        async get(nexusId) {
            return ENexusAPI.request(ENexusAPI.baseUrl + '/nexus/get', {nexusId});
        },
        
        async resources(nexusId) {
            return ENexusAPI.request(ENexusAPI.baseUrl + '/nexus/resources', {nexusId});
        },
        
        async skills(nexusId) {
            return ENexusAPI.request(ENexusAPI.baseUrl + '/nexus/skills', {nexusId});
        },
        
        async scenes(nexusId) {
            return ENexusAPI.request(ENexusAPI.baseUrl + '/nexus/scenes', {nexusId});
        },
        
        async command(data) {
            return ENexusAPI.request(ENexusAPI.baseUrl + '/nexus/command', data);
        }
    },
    
    // 初始化配置包
    initPackage: {
        async list(params = {}) {
            return ENexusAPI.request(ENexusAPI.baseUrl + '/init-package/list', params);
        },
        
        async generate(data) {
            return ENexusAPI.request(ENexusAPI.baseUrl + '/init-package/generate', data);
        },
        
        async send(packageId) {
            return ENexusAPI.request(ENexusAPI.baseUrl + '/init-package/send', {packageId});
        },
        
        async get(packageId) {
            return ENexusAPI.request(ENexusAPI.baseUrl + '/init-package/get', {packageId});
        },
        
        async status(packageId) {
            return ENexusAPI.request(ENexusAPI.baseUrl + '/init-package/status', {packageId});
        }
    },
    
    // 安全管理
    security: {
        async listUserKeys(params = {}) {
            return ENexusAPI.request(ENexusAPI.baseUrl + '/security/keys/user/list', params);
        },
        
        async generateUserKey(userId) {
            return ENexusAPI.request(ENexusAPI.baseUrl + '/security/keys/user/generate', {userId});
        },
        
        async revokeUserKey(keyId) {
            return ENexusAPI.request(ENexusAPI.baseUrl + '/security/keys/user/revoke', {keyId});
        },
        
        async listDomainKeys(params = {}) {
            return ENexusAPI.request(ENexusAPI.baseUrl + '/security/keys/domain/list', params);
        },
        
        async generateDomainKey(domainId) {
            return ENexusAPI.request(ENexusAPI.baseUrl + '/security/keys/domain/generate', {domainId});
        },
        
        async listSceneKeys(params = {}) {
            return ENexusAPI.request(ENexusAPI.baseUrl + '/security/keys/scene/list', params);
        },
        
        async generateSceneKey(sceneId, groupId) {
            return ENexusAPI.request(ENexusAPI.baseUrl + '/security/keys/scene/generate', {sceneId, groupId});
        },
        
        async rotateSceneKey(sceneId) {
            return ENexusAPI.request(ENexusAPI.baseUrl + '/security/keys/scene/rotate', {sceneId});
        },
        
        async listCertificates(params = {}) {
            return ENexusAPI.request(ENexusAPI.baseUrl + '/security/certificate/list', params);
        },
        
        async issueCertificate(data) {
            return ENexusAPI.request(ENexusAPI.baseUrl + '/security/certificate/issue', data);
        },
        
        async revokeCertificate(certId) {
            return ENexusAPI.request(ENexusAPI.baseUrl + '/security/certificate/revoke', {certId});
        },
        
        async listTokens(params = {}) {
            return ENexusAPI.request(ENexusAPI.baseUrl + '/security/token/list', params);
        },
        
        async generateToken(data) {
            return ENexusAPI.request(ENexusAPI.baseUrl + '/security/token/generate', data);
        },
        
        async revokeToken(tokenId) {
            return ENexusAPI.request(ENexusAPI.baseUrl + '/security/token/revoke', {tokenId});
        }
    },
    
    // 通信管理
    relay: {
        async sendCommand(data) {
            return ENexusAPI.request(ENexusAPI.baseUrl + '/relay/command/send', data);
        },
        
        async broadcastCommand(data) {
            return ENexusAPI.request(ENexusAPI.baseUrl + '/relay/command/broadcast', data);
        },
        
        async commandStatus(commandId) {
            return ENexusAPI.request(ENexusAPI.baseUrl + '/relay/command/status', {commandId});
        },
        
        async cancelCommand(commandId) {
            return ENexusAPI.request(ENexusAPI.baseUrl + '/relay/command/cancel', {commandId});
        }
    },
    
    // P2P 管理
    p2p: {
        async listApprovals(params = {}) {
            return ENexusAPI.request(ENexusAPI.baseUrl + '/p2p/approval/list', params);
        },
        
        async apply(data) {
            return ENexusAPI.request(ENexusAPI.baseUrl + '/p2p/approval/apply', data);
        },
        
        async approve(data) {
            return ENexusAPI.request(ENexusAPI.baseUrl + '/p2p/approval/approve', data);
        },
        
        async reject(data) {
            return ENexusAPI.request(ENexusAPI.baseUrl + '/p2p/approval/reject', data);
        },
        
        async revoke(approvalId) {
            return ENexusAPI.request(ENexusAPI.baseUrl + '/p2p/approval/revoke', {approvalId});
        },
        
        async getCredential(sourceNexusId, targetNexusId) {
            return ENexusAPI.request(ENexusAPI.baseUrl + '/p2p/credential/get', {sourceNexusId, targetNexusId});
        },
        
        async getUsageStats(approvalId) {
            return ENexusAPI.request(ENexusAPI.baseUrl + '/p2p/stats/usage', {approvalId});
        }
    }
};

// 北向协议 API
const NorthboundAPI = {
    baseUrl: '/api/north',
    
    async request(url, data = {}) {
        const response = await fetch(url, {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(data)
        });
        return await response.json();
    },
    
    async register(data) {
        return this.request(this.baseUrl + '/mcp/register', data);
    },
    
    async heartbeat(data) {
        return this.request(this.baseUrl + '/mcp/heartbeat', data);
    },
    
    async reportSubnet(data) {
        return this.request(this.baseUrl + '/mcp/subnet/report', data);
    },
    
    async reportCapability(data) {
        return this.request(this.baseUrl + '/mcp/capability/report', data);
    },
    
    async reportStatus(data) {
        return this.request(this.baseUrl + '/mcp/status/report', data);
    },
    
    async sendCommand(data) {
        return this.request(this.baseUrl + '/command/send', data);
    },
    
    async pushConfig(data) {
        return this.request(this.baseUrl + '/config/push', data);
    },
    
    async pushPolicy(data) {
        return this.request(this.baseUrl + '/policy/push', data);
    },
    
    async installSkill(data) {
        return this.request(this.baseUrl + '/skill/install', data);
    }
};
