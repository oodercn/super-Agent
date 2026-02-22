package net.ooder.nexus.service.impl;

import net.ooder.nexus.service.DomainPolicyService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class DomainPolicyServiceImpl implements DomainPolicyService {

    private static final Logger log = LoggerFactory.getLogger(DomainPolicyServiceImpl.class);

    private DomainPolicy currentPolicy = null;
    private boolean policyApplied = false;
    private final List<PolicyChangeListener> listeners = new CopyOnWriteArrayList<PolicyChangeListener>();

    public DomainPolicyServiceImpl() {
        log.info("DomainPolicyServiceImpl initialized with SDK 0.7.2");
    }

    @Override
    public CompletableFuture<DomainPolicy> getDomainPolicy(String domainId) {
        log.info("Getting domain policy for: {}", domainId);
        
        return CompletableFuture.supplyAsync(() -> {
            DomainPolicy policy = new DomainPolicy();
            policy.setPolicyId("policy-" + domainId);
            policy.setDomainId(domainId);
            policy.setPolicyName("默认域策略");
            policy.setPolicyVersion("1.0.0");
            policy.setUpdateTime(System.currentTimeMillis());
            
            StoragePolicy storagePolicy = new StoragePolicy();
            storagePolicy.setStorageType("LOCAL");
            storagePolicy.setMaxStorageSize(10L * 1024 * 1024 * 1024);
            storagePolicy.setEncryptionEnabled(true);
            storagePolicy.setAllowedPaths(Arrays.asList("/data", "/skills"));
            policy.setStoragePolicy(storagePolicy);
            
            SkillPolicy skillPolicy = new SkillPolicy();
            skillPolicy.setAllowedSkills(Arrays.asList("skill-*"));
            skillPolicy.setRequiredSkills(new ArrayList<String>());
            skillPolicy.setBlockedSkills(new ArrayList<String>());
            skillPolicy.setSkillSource("market");
            skillPolicy.setAutoUpdate(true);
            policy.setSkillPolicy(skillPolicy);
            
            SecurityPolicy securityPolicy = new SecurityPolicy();
            securityPolicy.setAuthMode("TOKEN");
            securityPolicy.setSessionTimeout(3600);
            securityPolicy.setMfaRequired(false);
            securityPolicy.setAllowedIpRanges(Arrays.asList("*"));
            securityPolicy.setPermissions(new HashMap<String, String>());
            policy.setSecurityPolicy(securityPolicy);
            
            NetworkPolicy networkPolicy = new NetworkPolicy();
            networkPolicy.setP2pEnabled(true);
            networkPolicy.setRelayEnabled(true);
            networkPolicy.setAllowedProtocols(Arrays.asList("TCP", "UDP", "WS"));
            networkPolicy.setMaxConnections(100);
            networkPolicy.setBandwidthLimit(0);
            policy.setNetworkPolicy(networkPolicy);
            
            return policy;
        });
    }

    @Override
    public CompletableFuture<PolicyApplyResult> applyPolicy(DomainPolicy policy) {
        log.info("Applying domain policy: {}", policy.getPolicyName());
        
        return CompletableFuture.supplyAsync(() -> {
            PolicyApplyResult result = new PolicyApplyResult();
            List<String> applied = new ArrayList<String>();
            List<String> failed = new ArrayList<String>();
            
            try {
                notifyProgress(10, "开始应用策略");
                
                if (policy.getStoragePolicy() != null) {
                    applyStoragePolicy(policy.getStoragePolicy());
                    applied.add("storage");
                    notifyProgress(30, "存储策略已应用");
                }
                
                if (policy.getSkillPolicy() != null) {
                    applySkillPolicy(policy.getSkillPolicy());
                    applied.add("skill");
                    notifyProgress(50, "技能策略已应用");
                }
                
                if (policy.getSecurityPolicy() != null) {
                    applySecurityPolicy(policy.getSecurityPolicy());
                    applied.add("security");
                    notifyProgress(70, "安全策略已应用");
                }
                
                if (policy.getNetworkPolicy() != null) {
                    applyNetworkPolicy(policy.getNetworkPolicy());
                    applied.add("network");
                    notifyProgress(90, "网络策略已应用");
                }
                
                DomainPolicy oldPolicy = currentPolicy;
                currentPolicy = policy;
                policyApplied = true;
                
                notifyProgress(100, "策略应用完成");
                notifyPolicyChanged(oldPolicy, policy);
                
                result.setSuccess(true);
                result.setMessage("策略应用成功");
                result.setAppliedComponents(applied);
                result.setFailedComponents(failed);
                
                log.info("Domain policy applied successfully");
            } catch (Exception e) {
                log.error("Failed to apply domain policy", e);
                result.setSuccess(false);
                result.setMessage("策略应用失败: " + e.getMessage());
                result.setAppliedComponents(applied);
                result.setFailedComponents(failed);
            }
            
            return result;
        });
    }

    private void applyStoragePolicy(StoragePolicy policy) {
        log.info("Applying storage policy: type={}, maxSize={}", 
            policy.getStorageType(), policy.getMaxStorageSize());
    }

    private void applySkillPolicy(SkillPolicy policy) {
        log.info("Applying skill policy: source={}, autoUpdate={}", 
            policy.getSkillSource(), policy.isAutoUpdate());
    }

    private void applySecurityPolicy(SecurityPolicy policy) {
        log.info("Applying security policy: authMode={}, sessionTimeout={}", 
            policy.getAuthMode(), policy.getSessionTimeout());
    }

    private void applyNetworkPolicy(NetworkPolicy policy) {
        log.info("Applying network policy: p2p={}, relay={}, maxConn={}", 
            policy.isP2pEnabled(), policy.isRelayEnabled(), policy.getMaxConnections());
    }

    @Override
    public DomainPolicy getCurrentPolicy() {
        return currentPolicy;
    }

    @Override
    public boolean isPolicyApplied() {
        return policyApplied;
    }

    @Override
    public CompletableFuture<Void> resetToDefault() {
        log.info("Resetting to default policy");
        
        return CompletableFuture.runAsync(() -> {
            currentPolicy = null;
            policyApplied = false;
            log.info("Policy reset to default");
        });
    }

    @Override
    public void addPolicyChangeListener(PolicyChangeListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removePolicyChangeListener(PolicyChangeListener listener) {
        listeners.remove(listener);
    }

    @Override
    public CompletableFuture<PolicyValidationResult> validatePolicy(DomainPolicy policy) {
        log.info("Validating policy: {}", policy.getPolicyName());
        
        return CompletableFuture.supplyAsync(() -> {
            PolicyValidationResult result = new PolicyValidationResult();
            List<String> errors = new ArrayList<String>();
            List<String> warnings = new ArrayList<String>();
            
            if (policy.getStoragePolicy() != null) {
                if (policy.getStoragePolicy().getMaxStorageSize() <= 0) {
                    errors.add("存储大小必须大于0");
                }
            }
            
            if (policy.getSecurityPolicy() != null) {
                if (policy.getSecurityPolicy().getSessionTimeout() < 60) {
                    warnings.add("会话超时时间过短，建议至少60秒");
                }
            }
            
            if (policy.getNetworkPolicy() != null) {
                if (policy.getNetworkPolicy().getMaxConnections() < 1) {
                    errors.add("最大连接数必须大于0");
                }
            }
            
            result.setValid(errors.isEmpty());
            result.setErrors(errors);
            result.setWarnings(warnings);
            
            return result;
        });
    }

    private void notifyPolicyChanged(DomainPolicy oldPolicy, DomainPolicy newPolicy) {
        for (PolicyChangeListener listener : listeners) {
            try {
                listener.onPolicyChanged(oldPolicy, newPolicy);
            } catch (Exception e) {
                log.warn("Listener error: {}", e.getMessage());
            }
        }
    }

    private void notifyProgress(int progress, String stage) {
        for (PolicyChangeListener listener : listeners) {
            try {
                listener.onPolicyApplyProgress(progress, stage);
            } catch (Exception e) {
                log.warn("Listener error: {}", e.getMessage());
            }
        }
    }
}
