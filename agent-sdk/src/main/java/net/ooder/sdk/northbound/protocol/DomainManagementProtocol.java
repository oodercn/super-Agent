package net.ooder.sdk.northbound.protocol;

import net.ooder.sdk.northbound.protocol.model.CreateDomainRequest;
import net.ooder.sdk.northbound.protocol.model.UpdateDomainRequest;
import net.ooder.sdk.northbound.protocol.model.DomainQuery;
import net.ooder.sdk.northbound.protocol.model.DomainInfo;
import net.ooder.sdk.northbound.protocol.model.DomainMember;
import net.ooder.sdk.northbound.protocol.model.AddMemberRequest;
import net.ooder.sdk.northbound.protocol.model.DomainPolicyConfig;
import net.ooder.sdk.northbound.protocol.model.InvitationRequest;
import net.ooder.sdk.northbound.protocol.model.DomainInvitation;
import net.ooder.sdk.northbound.protocol.model.DomainListener;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface DomainManagementProtocol {
    
    CompletableFuture<DomainInfo> createDomain(CreateDomainRequest request);
    
    CompletableFuture<DomainInfo> getDomain(String domainId);
    
    CompletableFuture<DomainInfo> updateDomain(String domainId, UpdateDomainRequest request);
    
    CompletableFuture<Void> deleteDomain(String domainId);
    
    CompletableFuture<List<DomainInfo>> listDomains(DomainQuery query);
    
    CompletableFuture<Void> addMember(String domainId, AddMemberRequest request);
    
    CompletableFuture<Void> removeMember(String domainId, String memberId);
    
    CompletableFuture<List<DomainMember>> listMembers(String domainId);
    
    CompletableFuture<Void> updateMemberRole(String domainId, String memberId, String newRole);
    
    CompletableFuture<Void> setDomainPolicy(String domainId, DomainPolicyConfig policy);
    
    CompletableFuture<DomainPolicyConfig> getDomainPolicy(String domainId);
    
    CompletableFuture<DomainInvitation> createInvitation(String domainId, InvitationRequest request);
    
    CompletableFuture<Void> acceptInvitation(String invitationId);
    
    CompletableFuture<Void> rejectInvitation(String invitationId);
    
    CompletableFuture<Void> revokeInvitation(String invitationId);
    
    CompletableFuture<List<DomainInvitation>> listPendingInvitations(String domainId);
    
    CompletableFuture<List<DomainInvitation>> listMyInvitations();
    
    void addDomainListener(DomainListener listener);
    
    void removeDomainListener(DomainListener listener);
}
