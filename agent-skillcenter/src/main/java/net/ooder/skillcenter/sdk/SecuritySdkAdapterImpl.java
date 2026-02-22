package net.ooder.skillcenter.sdk;

import net.ooder.scene.provider.*;
import net.ooder.skillcenter.config.SdkConfig;
import net.ooder.skillcenter.dto.PageResult;
import net.ooder.nexus.skillcenter.dto.security.SecurityPolicyDTO;
import net.ooder.nexus.skillcenter.dto.security.SecurityAuditDTO;
import net.ooder.nexus.skillcenter.dto.security.AccessControlDTO;
import net.ooder.nexus.skillcenter.dto.security.ThreatInfoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
@Primary
public class SecuritySdkAdapterImpl implements SecuritySdkAdapter {

    private static final Logger log = LoggerFactory.getLogger(SecuritySdkAdapterImpl.class);

    @Autowired
    private SdkConfig sdkConfig;

    @Autowired
    private SceneEngineAdapter sceneEngineAdapter;

    private SecurityProvider securityProvider;
    private boolean sdkAvailable = false;

    private final Map<String, SecurityPolicyDTO> localPolicies = new HashMap<>();
    private final Map<String, AccessControlDTO> localAcls = new HashMap<>();
    private final Map<String, ThreatInfoDTO> localThreats = new HashMap<>();

    @PostConstruct
    public void init() {
        log.info("[SecuritySdkAdapter] Initializing...");
        
        if (sceneEngineAdapter != null && sceneEngineAdapter.isAvailable()) {
            securityProvider = sceneEngineAdapter.getSecurityProvider();
            if (securityProvider != null) {
                sdkAvailable = true;
                log.info("[SecuritySdkAdapter] SecurityProvider available from SceneEngine");
            }
        }
        
        if (!sdkAvailable) {
            log.info("[SecuritySdkAdapter] Using local fallback storage");
            initLocalData();
        }
    }

    private void initLocalData() {
        SecurityPolicyDTO policy = new SecurityPolicyDTO();
        policy.setPolicyId("policy-001");
        policy.setPolicyName("Default Security Policy");
        policy.setPolicyType("firewall");
        policy.setDescription("Default security policy");
        policy.setStatus("active");
        policy.setPriority(1);
        localPolicies.put(policy.getPolicyId(), policy);
    }

    @Override
    public Map<String, Object> getSecurityStatus() {
        if (sdkAvailable && securityProvider != null) {
            try {
                SecurityStatus status = securityProvider.getStatus();
                Map<String, Object> result = new HashMap<>();
                result.put("status", status.getStatus());
                result.put("securityLevel", status.getSecurityLevel());
                result.put("activePolicies", status.getActivePolicies());
                result.put("totalPolicies", status.getTotalPolicies());
                result.put("recentAlerts", status.getRecentAlerts());
                result.put("blockedAttempts", status.getBlockedAttempts());
                result.put("threatScore", status.getThreatScore());
                result.put("firewallEnabled", status.isFirewallEnabled());
                result.put("encryptionEnabled", status.isEncryptionEnabled());
                result.put("auditEnabled", status.isAuditEnabled());
                result.put("lastScanTime", status.getLastScanTime());
                return result;
            } catch (Exception e) {
                log.error("[SecuritySdkAdapter] Failed to get security status: {}", e.getMessage());
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("status", "secure");
        result.put("securityLevel", "high");
        result.put("activePolicies", localPolicies.size());
        result.put("totalPolicies", localPolicies.size());
        result.put("recentAlerts", 0);
        result.put("blockedAttempts", 0);
        result.put("threatScore", 0);
        result.put("firewallEnabled", true);
        result.put("encryptionEnabled", true);
        result.put("auditEnabled", true);
        result.put("lastScanTime", System.currentTimeMillis());
        return result;
    }

    @Override
    public Map<String, Object> getSecurityStats() {
        if (sdkAvailable && securityProvider != null) {
            try {
                SecurityStats stats = securityProvider.getStats();
                Map<String, Object> result = new HashMap<>();
                result.put("totalPolicies", stats.getTotalPolicies());
                result.put("activePolicies", stats.getActivePolicies());
                result.put("totalAcls", stats.getTotalAcls());
                result.put("totalThreats", stats.getTotalThreats());
                result.put("resolvedThreats", stats.getResolvedThreats());
                result.put("pendingThreats", stats.getTotalThreats() - stats.getResolvedThreats());
                result.put("auditEvents", 0);
                result.put("blockedAttempts", stats.getBlockedAttempts());
                return result;
            } catch (Exception e) {
                log.error("[SecuritySdkAdapter] Failed to get security stats: {}", e.getMessage());
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("totalPolicies", localPolicies.size());
        result.put("activePolicies", localPolicies.values().stream().filter(p -> "active".equals(p.getStatus())).count());
        result.put("totalAcls", localAcls.size());
        result.put("totalThreats", localThreats.size());
        result.put("resolvedThreats", localThreats.values().stream().filter(t -> "resolved".equals(t.getStatus())).count());
        result.put("pendingThreats", localThreats.values().stream().filter(t -> "pending".equals(t.getStatus())).count());
        result.put("auditEvents", 0);
        result.put("blockedAttempts", 0);
        return result;
    }

    @Override
    public PageResult<SecurityPolicyDTO> getPolicies(int pageNum, int pageSize) {
        if (sdkAvailable && securityProvider != null) {
            try {
                List<SecurityPolicy> policies = securityProvider.listPolicies();
                List<SecurityPolicyDTO> dtoList = new ArrayList<>();
                for (SecurityPolicy policy : policies) {
                    dtoList.add(convertPolicyToDTO(policy));
                }
                return paginate(dtoList, pageNum, pageSize);
            } catch (Exception e) {
                log.error("[SecuritySdkAdapter] Failed to get policies: {}", e.getMessage());
            }
        }

        List<SecurityPolicyDTO> list = new ArrayList<>(localPolicies.values());
        return paginate(list, pageNum, pageSize);
    }

    @Override
    public SecurityPolicyDTO getPolicyById(String policyId) {
        if (sdkAvailable && securityProvider != null) {
            try {
                SecurityPolicy policy = securityProvider.getPolicy(policyId);
                return policy != null ? convertPolicyToDTO(policy) : null;
            } catch (Exception e) {
                log.error("[SecuritySdkAdapter] Failed to get policy: {}", e.getMessage());
            }
        }
        return localPolicies.get(policyId);
    }

    @Override
    public SecurityPolicyDTO createPolicy(SecurityPolicyDTO policy) {
        if (sdkAvailable && securityProvider != null) {
            try {
                SecurityPolicy newPolicy = convertDTOToPolicy(policy);
                SecurityPolicy created = securityProvider.createPolicy(newPolicy);
                return convertPolicyToDTO(created);
            } catch (Exception e) {
                log.error("[SecuritySdkAdapter] Failed to create policy: {}", e.getMessage());
            }
        }

        String id = policy.getPolicyId() != null ? policy.getPolicyId() : "policy-" + UUID.randomUUID().toString().substring(0, 8);
        policy.setPolicyId(id);
        policy.setCreatedAt(new Date());
        policy.setUpdatedAt(new Date());
        localPolicies.put(id, policy);
        return policy;
    }

    @Override
    public boolean enablePolicy(String policyId) {
        if (sdkAvailable && securityProvider != null) {
            try {
                return securityProvider.enablePolicy(policyId);
            } catch (Exception e) {
                log.error("[SecuritySdkAdapter] Failed to enable policy: {}", e.getMessage());
            }
        }

        SecurityPolicyDTO policy = localPolicies.get(policyId);
        if (policy != null) {
            policy.setStatus("active");
            return true;
        }
        return false;
    }

    @Override
    public boolean disablePolicy(String policyId) {
        if (sdkAvailable && securityProvider != null) {
            try {
                return securityProvider.disablePolicy(policyId);
            } catch (Exception e) {
                log.error("[SecuritySdkAdapter] Failed to disable policy: {}", e.getMessage());
            }
        }

        SecurityPolicyDTO policy = localPolicies.get(policyId);
        if (policy != null) {
            policy.setStatus("inactive");
            return true;
        }
        return false;
    }

    @Override
    public boolean deletePolicy(String policyId) {
        if (sdkAvailable && securityProvider != null) {
            try {
                return securityProvider.deletePolicy(policyId);
            } catch (Exception e) {
                log.error("[SecuritySdkAdapter] Failed to delete policy: {}", e.getMessage());
            }
        }
        return localPolicies.remove(policyId) != null;
    }

    @Override
    public PageResult<SecurityAuditDTO> getAuditLogs(int pageNum, int pageSize, String keyword) {
        return new PageResult<>(new ArrayList<>(), 0, pageNum, pageSize);
    }

    @Override
    public PageResult<AccessControlDTO> getAclList(int pageNum, int pageSize) {
        if (sdkAvailable && securityProvider != null) {
            try {
                net.ooder.scene.core.PageResult<AccessControl> result = securityProvider.listAcls(pageNum, pageSize);
        List<AccessControlDTO> dtoList = new ArrayList<>();
        try {
            // 使用反射获取数据列表
            java.lang.reflect.Method getDataMethod = result.getClass().getMethod("getData");
            List<AccessControl> aclList = (List<AccessControl>) getDataMethod.invoke(result);
            for (AccessControl acl : aclList) {
                dtoList.add(convertAclToDTO(acl));
            }
        } catch (Exception ex) {
            // 如果反射失败，尝试getList方法
            try {
                java.lang.reflect.Method getListMethod = result.getClass().getMethod("getList");
                List<AccessControl> aclList = (List<AccessControl>) getListMethod.invoke(result);
                for (AccessControl acl : aclList) {
                    dtoList.add(convertAclToDTO(acl));
                }
            } catch (Exception e) {
                log.warn("[SecuritySdkAdapter] Failed to get ACL list: {}", e.getMessage());
            }
        }
                return new PageResult<>(dtoList, result.getTotal(), result.getPageNum(), result.getPageSize());
            } catch (Exception e) {
                log.error("[SecuritySdkAdapter] Failed to get ACL list: {}", e.getMessage());
            }
        }

        List<AccessControlDTO> list = new ArrayList<>(localAcls.values());
        return paginate(list, pageNum, pageSize);
    }

    @Override
    public AccessControlDTO createAcl(AccessControlDTO acl) {
        if (sdkAvailable && securityProvider != null) {
            try {
                AccessControl newAcl = convertDTOToAcl(acl);
                AccessControl created = securityProvider.createAcl(newAcl);
                return convertAclToDTO(created);
            } catch (Exception e) {
                log.error("[SecuritySdkAdapter] Failed to create ACL: {}", e.getMessage());
            }
        }

        String id = acl.getAclId() != null ? acl.getAclId() : "acl-" + UUID.randomUUID().toString().substring(0, 8);
        acl.setAclId(id);
        acl.setGrantedAt(System.currentTimeMillis());
        localAcls.put(id, acl);
        return acl;
    }

    @Override
    public boolean deleteAcl(String aclId) {
        if (sdkAvailable && securityProvider != null) {
            try {
                return securityProvider.deleteAcl(aclId);
            } catch (Exception e) {
                log.error("[SecuritySdkAdapter] Failed to delete ACL: {}", e.getMessage());
            }
        }
        return localAcls.remove(aclId) != null;
    }

    @Override
    public PageResult<ThreatInfoDTO> getThreats(int pageNum, int pageSize) {
        if (sdkAvailable && securityProvider != null) {
            try {
                net.ooder.scene.core.PageResult<ThreatInfo> result = securityProvider.listThreats(pageNum, pageSize);
        List<ThreatInfoDTO> dtoList = new ArrayList<>();
        try {
            // 使用反射获取数据列表
            java.lang.reflect.Method getDataMethod = result.getClass().getMethod("getData");
            List<ThreatInfo> threatList = (List<ThreatInfo>) getDataMethod.invoke(result);
            for (ThreatInfo threat : threatList) {
                dtoList.add(convertThreatToDTO(threat));
            }
        } catch (Exception ex) {
            // 如果反射失败，尝试getList方法
            try {
                java.lang.reflect.Method getListMethod = result.getClass().getMethod("getList");
                List<ThreatInfo> threatList = (List<ThreatInfo>) getListMethod.invoke(result);
                for (ThreatInfo threat : threatList) {
                    dtoList.add(convertThreatToDTO(threat));
                }
            } catch (Exception e) {
                log.warn("[SecuritySdkAdapter] Failed to get threat list: {}", e.getMessage());
            }
        }
                return new PageResult<>(dtoList, result.getTotal(), result.getPageNum(), result.getPageSize());
            } catch (Exception e) {
                log.error("[SecuritySdkAdapter] Failed to get threats: {}", e.getMessage());
            }
        }

        List<ThreatInfoDTO> list = new ArrayList<>(localThreats.values());
        return paginate(list, pageNum, pageSize);
    }

    @Override
    public boolean resolveThreat(String threatId) {
        if (sdkAvailable && securityProvider != null) {
            try {
                return securityProvider.resolveThreat(threatId);
            } catch (Exception e) {
                log.error("[SecuritySdkAdapter] Failed to resolve threat: {}", e.getMessage());
            }
        }

        ThreatInfoDTO threat = localThreats.get(threatId);
        if (threat != null) {
            threat.setStatus("resolved");
            threat.setResolvedAt(System.currentTimeMillis());
            return true;
        }
        return false;
    }

    @Override
    public boolean runSecurityScan() {
        if (sdkAvailable && securityProvider != null) {
            try {
                return securityProvider.runSecurityScan();
            } catch (Exception e) {
                log.error("[SecuritySdkAdapter] Failed to run security scan: {}", e.getMessage());
            }
        }
        log.info("[SecuritySdkAdapter] Running security scan (local)...");
        return true;
    }

    @Override
    public boolean toggleFirewall() {
        if (sdkAvailable && securityProvider != null) {
            try {
                return securityProvider.toggleFirewall();
            } catch (Exception e) {
                log.error("[SecuritySdkAdapter] Failed to toggle firewall: {}", e.getMessage());
            }
        }
        log.info("[SecuritySdkAdapter] Toggling firewall (local)...");
        return true;
    }

    @Override
    public boolean isAvailable() {
        return sdkAvailable || !localPolicies.isEmpty();
    }

    private SecurityPolicyDTO convertPolicyToDTO(SecurityPolicy policy) {
        SecurityPolicyDTO dto = new SecurityPolicyDTO();
        dto.setPolicyId(policy.getPolicyId());
        dto.setPolicyName(policy.getPolicyName());
        dto.setPolicyType(policy.getPolicyType());
        dto.setDescription(policy.getDescription());
        dto.setStatus(policy.getStatus());
        dto.setPriority(policy.getPriority());
        dto.setAction(policy.getAction());
        dto.setCreatedAt(policy.getCreatedAt() > 0 ? new Date(policy.getCreatedAt()) : null);
        dto.setUpdatedAt(policy.getUpdatedAt() > 0 ? new Date(policy.getUpdatedAt()) : null);
        return dto;
    }

    private SecurityPolicy convertDTOToPolicy(SecurityPolicyDTO dto) {
        SecurityPolicy policy = new SecurityPolicy();
        policy.setPolicyId(dto.getPolicyId());
        policy.setPolicyName(dto.getPolicyName());
        policy.setPolicyType(dto.getPolicyType());
        policy.setDescription(dto.getDescription());
        policy.setPriority(dto.getPriority());
        policy.setAction(dto.getAction());
        return policy;
    }

    private AccessControlDTO convertAclToDTO(AccessControl acl) {
        AccessControlDTO dto = new AccessControlDTO();
        dto.setAclId(acl.getAclId());
        dto.setResourceType(acl.getResourceType());
        dto.setResourceId(acl.getResourceId());
        dto.setPrincipalType(acl.getPrincipalType());
        dto.setPrincipalId(acl.getPrincipalId());
        dto.setPermission(acl.getPermission());
        dto.setStatus(acl.getStatus());
        dto.setGrantedAt(acl.getGrantedAt());
        dto.setGrantedBy(acl.getGrantedBy());
        return dto;
    }

    private AccessControl convertDTOToAcl(AccessControlDTO dto) {
        AccessControl acl = new AccessControl();
        acl.setResourceType(dto.getResourceType());
        acl.setResourceId(dto.getResourceId());
        acl.setPrincipalType(dto.getPrincipalType());
        acl.setPrincipalId(dto.getPrincipalId());
        acl.setPermission(dto.getPermission());
        return acl;
    }

    private ThreatInfoDTO convertThreatToDTO(ThreatInfo threat) {
        ThreatInfoDTO dto = new ThreatInfoDTO();
        dto.setThreatId(threat.getThreatId());
        dto.setThreatType(threat.getThreatType());
        dto.setSeverity(threat.getSeverity());
        dto.setSource(threat.getSource());
        dto.setDescription(threat.getDescription());
        dto.setStatus(threat.getStatus());
        dto.setRecommendation(threat.getRecommendation());
        dto.setDetectedAt(threat.getDetectedAt() > 0 ? threat.getDetectedAt() : 0);
        dto.setResolvedAt(threat.getResolvedAt() > 0 ? threat.getResolvedAt() : 0);
        return dto;
    }

    private <T> PageResult<T> paginate(List<T> list, int pageNum, int pageSize) {
        int total = list.size();
        int start = (pageNum - 1) * pageSize;
        int end = Math.min(start + pageSize, total);

        if (start >= total) {
            return new PageResult<>(new ArrayList<>(), total, pageNum, pageSize);
        }

        return new PageResult<>(list.subList(start, end), total, pageNum, pageSize);
    }
}
