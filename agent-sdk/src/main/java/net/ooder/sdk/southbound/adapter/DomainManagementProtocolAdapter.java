
package net.ooder.sdk.southbound.adapter;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import net.ooder.sdk.southbound.adapter.model.DomainConfig;
import net.ooder.sdk.southbound.adapter.model.DomainInfo;
import net.ooder.sdk.southbound.adapter.model.DomainMember;

public interface DomainManagementProtocolAdapter {
    
    CompletableFuture<DomainInfo> createDomain(DomainConfig config);
    
    CompletableFuture<Void> deleteDomain(String domainId);
    
    CompletableFuture<DomainInfo> getDomain(String domainId);
    
    CompletableFuture<List<DomainInfo>> listDomains();
    
    CompletableFuture<Void> addDomainMember(String domainId, String agentId, String role);
    
    CompletableFuture<Void> removeDomainMember(String domainId, String agentId);
    
    CompletableFuture<List<DomainMember>> getDomainMembers(String domainId);
    
    CompletableFuture<Void> updateMemberRole(String domainId, String agentId, String newRole);
    
    CompletableFuture<Boolean> isMemberInDomain(String domainId, String agentId);
}
