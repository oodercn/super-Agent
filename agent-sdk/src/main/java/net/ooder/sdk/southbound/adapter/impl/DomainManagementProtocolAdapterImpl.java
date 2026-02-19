
package net.ooder.sdk.southbound.adapter.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import net.ooder.sdk.southbound.adapter.DomainManagementProtocolAdapter;
import net.ooder.sdk.southbound.adapter.model.DomainConfig;
import net.ooder.sdk.southbound.adapter.model.DomainInfo;
import net.ooder.sdk.southbound.adapter.model.DomainMember;

public class DomainManagementProtocolAdapterImpl implements DomainManagementProtocolAdapter {
    
    private final Map<String, DomainInfo> domains = new ConcurrentHashMap<>();
    private final Map<String, Map<String, DomainMember>> domainMembers = new ConcurrentHashMap<>();
    
    public DomainManagementProtocolAdapterImpl() {
    }
    
    @Override
    public CompletableFuture<DomainInfo> createDomain(DomainConfig config) {
        return CompletableFuture.supplyAsync(() -> {
            DomainInfo domain = new DomainInfo();
            domain.setDomainId(config.getDomainId() != null ? config.getDomainId() : generateDomainId());
            domain.setDomainName(config.getDomainName());
            domain.setDomainType(config.getDomainType());
            domain.setOwner(config.getOwner());
            domain.setMaxMembers(config.getMaxMembers());
            domain.setPolicies(config.getPolicies());
            domain.setAllowedRoles(config.getAllowedRoles());
            domain.setStatus("active");
            domain.setMemberCount(0);
            domain.setCreatedAt(System.currentTimeMillis());
            domain.setUpdatedAt(System.currentTimeMillis());
            
            domains.put(domain.getDomainId(), domain);
            domainMembers.put(domain.getDomainId(), new ConcurrentHashMap<>());
            
            return domain;
        });
    }
    
    @Override
    public CompletableFuture<Void> deleteDomain(String domainId) {
        return CompletableFuture.runAsync(() -> {
            domains.remove(domainId);
            domainMembers.remove(domainId);
        });
    }
    
    @Override
    public CompletableFuture<DomainInfo> getDomain(String domainId) {
        return CompletableFuture.supplyAsync(() -> domains.get(domainId));
    }
    
    @Override
    public CompletableFuture<List<DomainInfo>> listDomains() {
        return CompletableFuture.supplyAsync(() -> new ArrayList<>(domains.values()));
    }
    
    @Override
    public CompletableFuture<Void> addDomainMember(String domainId, String agentId, String role) {
        return CompletableFuture.runAsync(() -> {
            DomainInfo domain = domains.get(domainId);
            if (domain == null) {
                throw new RuntimeException("Domain not found: " + domainId);
            }
            
            Map<String, DomainMember> members = domainMembers.get(domainId);
            if (members == null) {
                members = new ConcurrentHashMap<>();
                domainMembers.put(domainId, members);
            }
            
            DomainMember member = new DomainMember();
            member.setAgentId(agentId);
            member.setDomainId(domainId);
            member.setRole(role);
            member.setStatus("active");
            member.setJoinedAt(System.currentTimeMillis());
            member.setLastActiveAt(System.currentTimeMillis());
            
            members.put(agentId, member);
            domain.setMemberCount(members.size());
            domain.setUpdatedAt(System.currentTimeMillis());
        });
    }
    
    @Override
    public CompletableFuture<Void> removeDomainMember(String domainId, String agentId) {
        return CompletableFuture.runAsync(() -> {
            Map<String, DomainMember> members = domainMembers.get(domainId);
            if (members != null) {
                members.remove(agentId);
                
                DomainInfo domain = domains.get(domainId);
                if (domain != null) {
                    domain.setMemberCount(members.size());
                    domain.setUpdatedAt(System.currentTimeMillis());
                }
            }
        });
    }
    
    @Override
    public CompletableFuture<List<DomainMember>> getDomainMembers(String domainId) {
        return CompletableFuture.supplyAsync(() -> {
            Map<String, DomainMember> members = domainMembers.get(domainId);
            if (members == null) {
                return new ArrayList<>();
            }
            return new ArrayList<>(members.values());
        });
    }
    
    @Override
    public CompletableFuture<Void> updateMemberRole(String domainId, String agentId, String newRole) {
        return CompletableFuture.runAsync(() -> {
            Map<String, DomainMember> members = domainMembers.get(domainId);
            if (members != null) {
                DomainMember member = members.get(agentId);
                if (member != null) {
                    member.setRole(newRole);
                    member.setLastActiveAt(System.currentTimeMillis());
                }
            }
        });
    }
    
    @Override
    public CompletableFuture<Boolean> isMemberInDomain(String domainId, String agentId) {
        return CompletableFuture.supplyAsync(() -> {
            Map<String, DomainMember> members = domainMembers.get(domainId);
            return members != null && members.containsKey(agentId);
        });
    }
    
    private String generateDomainId() {
        return "domain_" + System.currentTimeMillis() + "_" + (int)(Math.random() * 10000);
    }
}
