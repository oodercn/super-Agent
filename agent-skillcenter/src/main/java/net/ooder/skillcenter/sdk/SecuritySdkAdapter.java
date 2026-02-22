package net.ooder.skillcenter.sdk;

import net.ooder.skillcenter.dto.PageResult;
import net.ooder.nexus.skillcenter.dto.security.SecurityPolicyDTO;
import net.ooder.nexus.skillcenter.dto.security.SecurityAuditDTO;
import net.ooder.nexus.skillcenter.dto.security.AccessControlDTO;
import net.ooder.nexus.skillcenter.dto.security.ThreatInfoDTO;

import java.util.Map;

public interface SecuritySdkAdapter {
    
    Map<String, Object> getSecurityStatus();
    
    Map<String, Object> getSecurityStats();
    
    PageResult<SecurityPolicyDTO> getPolicies(int pageNum, int pageSize);
    
    SecurityPolicyDTO getPolicyById(String policyId);
    
    SecurityPolicyDTO createPolicy(SecurityPolicyDTO policy);
    
    boolean enablePolicy(String policyId);
    
    boolean disablePolicy(String policyId);
    
    boolean deletePolicy(String policyId);
    
    PageResult<SecurityAuditDTO> getAuditLogs(int pageNum, int pageSize, String keyword);
    
    PageResult<AccessControlDTO> getAclList(int pageNum, int pageSize);
    
    AccessControlDTO createAcl(AccessControlDTO acl);
    
    boolean deleteAcl(String aclId);
    
    PageResult<ThreatInfoDTO> getThreats(int pageNum, int pageSize);
    
    boolean resolveThreat(String threatId);
    
    boolean runSecurityScan();
    
    boolean toggleFirewall();
    
    boolean isAvailable();
}
