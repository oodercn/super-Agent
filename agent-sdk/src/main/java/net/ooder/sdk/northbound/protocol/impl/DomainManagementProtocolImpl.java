package net.ooder.sdk.northbound.protocol.impl;

import net.ooder.sdk.northbound.protocol.DomainManagementProtocol;
import net.ooder.sdk.northbound.protocol.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;

public class DomainManagementProtocolImpl implements DomainManagementProtocol {
    
    private static final Logger log = LoggerFactory.getLogger(DomainManagementProtocolImpl.class);
    
    private final Map<String, DomainInfo> domains;
    private final Map<String, List<DomainMember>> domainMembers;
    private final Map<String, DomainPolicyConfig> domainPolicies;
    private final Map<String, DomainInvitation> invitations;
    private final List<DomainListener> listeners;
    private final ExecutorService executor;
    
    public DomainManagementProtocolImpl() {
        this.domains = new ConcurrentHashMap<String, DomainInfo>();
        this.domainMembers = new ConcurrentHashMap<String, List<DomainMember>>();
        this.domainPolicies = new ConcurrentHashMap<String, DomainPolicyConfig>();
        this.invitations = new ConcurrentHashMap<String, DomainInvitation>();
        this.listeners = new CopyOnWriteArrayList<DomainListener>();
        this.executor = Executors.newCachedThreadPool();
        log.info("DomainManagementProtocolImpl initialized");
    }
    
    @Override
    public CompletableFuture<DomainInfo> createDomain(CreateDomainRequest request) {
        return CompletableFuture.supplyAsync(() -> {
            log.info("Creating domain: name={}", request.getDomainName());
            
            DomainInfo domain = new DomainInfo();
            domain.setDomainId("domain-" + UUID.randomUUID().toString().substring(0, 8));
            domain.setDomainName(request.getDomainName());
            domain.setDomainType(request.getDomainType());
            domain.setOwnerId(request.getOwnerId());
            domain.setMemberCount(1);
            domain.setStatus(DomainStatus.ACTIVE);
            domain.setCreatedAt(System.currentTimeMillis());
            domain.setUpdatedAt(System.currentTimeMillis());
            domain.setConfig(request.getConfig());
            
            domains.put(domain.getDomainId(), domain);
            domainMembers.put(domain.getDomainId(), new CopyOnWriteArrayList<DomainMember>());
            
            DomainMember owner = new DomainMember();
            owner.setMemberId(request.getOwnerId());
            owner.setMemberName("Owner");
            owner.setDomainRole("OWNER");
            owner.setStatus(MemberStatus.ACTIVE);
            owner.setJoinedAt(System.currentTimeMillis());
            owner.setLastActiveAt(System.currentTimeMillis());
            domainMembers.get(domain.getDomainId()).add(owner);
            
            notifyDomainCreated(domain);
            log.info("Domain created: id={}", domain.getDomainId());
            return domain;
        }, executor);
    }
    
    @Override
    public CompletableFuture<DomainInfo> getDomain(String domainId) {
        return CompletableFuture.supplyAsync(() -> domains.get(domainId), executor);
    }
    
    @Override
    public CompletableFuture<DomainInfo> updateDomain(String domainId, UpdateDomainRequest request) {
        return CompletableFuture.supplyAsync(() -> {
            log.info("Updating domain: id={}", domainId);
            
            DomainInfo domain = domains.get(domainId);
            if (domain != null) {
                if (request.getDomainName() != null) {
                    domain.setDomainName(request.getDomainName());
                }
                if (request.getConfig() != null) {
                    domain.setConfig(request.getConfig());
                }
                domain.setUpdatedAt(System.currentTimeMillis());
                notifyDomainUpdated(domain);
                log.info("Domain updated: id={}", domainId);
            }
            return domain;
        }, executor);
    }
    
    @Override
    public CompletableFuture<Void> deleteDomain(String domainId) {
        return CompletableFuture.runAsync(() -> {
            log.info("Deleting domain: id={}", domainId);
            
            DomainInfo removed = domains.remove(domainId);
            if (removed != null) {
                domainMembers.remove(domainId);
                domainPolicies.remove(domainId);
                notifyDomainDeleted(domainId);
                log.info("Domain deleted: id={}", domainId);
            }
        }, executor);
    }
    
    @Override
    public CompletableFuture<List<DomainInfo>> listDomains(DomainQuery query) {
        return CompletableFuture.supplyAsync(() -> {
            List<DomainInfo> result = new ArrayList<DomainInfo>();
            for (DomainInfo domain : domains.values()) {
                if (query.getDomainType() != null && !query.getDomainType().equals(domain.getDomainType())) {
                    continue;
                }
                if (query.getOwnerId() != null && !query.getOwnerId().equals(domain.getOwnerId())) {
                    continue;
                }
                result.add(domain);
            }
            return result;
        }, executor);
    }
    
    @Override
    public CompletableFuture<Void> addMember(String domainId, AddMemberRequest request) {
        return CompletableFuture.runAsync(() -> {
            log.info("Adding member to domain: domainId={}, memberId={}", domainId, request.getMemberId());
            
            List<DomainMember> members = domainMembers.get(domainId);
            if (members != null) {
                DomainMember member = new DomainMember();
                member.setMemberId(request.getMemberId());
                member.setMemberName(request.getMemberName());
                member.setDomainRole(request.getDomainRole());
                member.setStatus(MemberStatus.ACTIVE);
                member.setJoinedAt(System.currentTimeMillis());
                member.setLastActiveAt(System.currentTimeMillis());
                
                members.add(member);
                
                DomainInfo domain = domains.get(domainId);
                if (domain != null) {
                    domain.setMemberCount(members.size());
                }
                
                notifyMemberAdded(domainId, member);
                log.info("Member added to domain: domainId={}, memberId={}", domainId, request.getMemberId());
            }
        }, executor);
    }
    
    @Override
    public CompletableFuture<Void> removeMember(String domainId, String memberId) {
        return CompletableFuture.runAsync(() -> {
            log.info("Removing member from domain: domainId={}, memberId={}", domainId, memberId);
            
            List<DomainMember> members = domainMembers.get(domainId);
            if (members != null) {
                members.removeIf(m -> m.getMemberId().equals(memberId));
                
                DomainInfo domain = domains.get(domainId);
                if (domain != null) {
                    domain.setMemberCount(members.size());
                }
                
                notifyMemberRemoved(domainId, memberId);
                log.info("Member removed from domain: domainId={}, memberId={}", domainId, memberId);
            }
        }, executor);
    }
    
    @Override
    public CompletableFuture<List<DomainMember>> listMembers(String domainId) {
        return CompletableFuture.supplyAsync(() -> {
            List<DomainMember> members = domainMembers.get(domainId);
            return members != null ? new ArrayList<DomainMember>(members) : new ArrayList<DomainMember>();
        }, executor);
    }
    
    @Override
    public CompletableFuture<Void> updateMemberRole(String domainId, String memberId, String newRole) {
        return CompletableFuture.runAsync(() -> {
            log.info("Updating member role: domainId={}, memberId={}, newRole={}", domainId, memberId, newRole);
            
            List<DomainMember> members = domainMembers.get(domainId);
            if (members != null) {
                for (DomainMember member : members) {
                    if (member.getMemberId().equals(memberId)) {
                        member.setDomainRole(newRole);
                        log.info("Member role updated: domainId={}, memberId={}", domainId, memberId);
                        break;
                    }
                }
            }
        }, executor);
    }
    
    @Override
    public CompletableFuture<Void> setDomainPolicy(String domainId, DomainPolicyConfig policy) {
        return CompletableFuture.runAsync(() -> {
            log.info("Setting domain policy: domainId={}", domainId);
            policy.setDomainId(domainId);
            domainPolicies.put(domainId, policy);
            notifyPolicyUpdated(domainId, policy);
            log.info("Domain policy set: domainId={}", domainId);
        }, executor);
    }
    
    @Override
    public CompletableFuture<DomainPolicyConfig> getDomainPolicy(String domainId) {
        return CompletableFuture.supplyAsync(() -> domainPolicies.get(domainId), executor);
    }
    
    @Override
    public CompletableFuture<DomainInvitation> createInvitation(String domainId, InvitationRequest request) {
        return CompletableFuture.supplyAsync(() -> {
            log.info("Creating invitation: domainId={}, targetId={}", domainId, request.getTargetId());
            
            DomainInfo domain = domains.get(domainId);
            if (domain != null) {
                DomainInvitation invitation = new DomainInvitation();
                invitation.setInvitationId("inv-" + UUID.randomUUID().toString().substring(0, 8));
                invitation.setDomainId(domainId);
                invitation.setDomainName(domain.getDomainName());
                invitation.setTargetId(request.getTargetId());
                invitation.setCreatedAt(System.currentTimeMillis());
                invitation.setExpiresAt(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000);
                invitation.setStatus(InvitationStatus.PENDING);
                
                invitations.put(invitation.getInvitationId(), invitation);
                log.info("Invitation created: id={}", invitation.getInvitationId());
                return invitation;
            }
            return null;
        }, executor);
    }
    
    @Override
    public CompletableFuture<Void> acceptInvitation(String invitationId) {
        return CompletableFuture.runAsync(() -> {
            DomainInvitation inv = invitations.get(invitationId);
            if (inv != null) {
                inv.setStatus(InvitationStatus.ACCEPTED);
                log.info("Invitation accepted: id={}", invitationId);
            }
        }, executor);
    }
    
    @Override
    public CompletableFuture<Void> rejectInvitation(String invitationId) {
        return CompletableFuture.runAsync(() -> {
            DomainInvitation inv = invitations.get(invitationId);
            if (inv != null) {
                inv.setStatus(InvitationStatus.REJECTED);
                log.info("Invitation rejected: id={}", invitationId);
            }
        }, executor);
    }
    
    @Override
    public CompletableFuture<Void> revokeInvitation(String invitationId) {
        return CompletableFuture.runAsync(() -> {
            DomainInvitation inv = invitations.remove(invitationId);
            if (inv != null) {
                log.info("Invitation revoked: id={}", invitationId);
            }
        }, executor);
    }
    
    @Override
    public CompletableFuture<List<DomainInvitation>> listPendingInvitations(String domainId) {
        return CompletableFuture.supplyAsync(() -> {
            List<DomainInvitation> result = new ArrayList<DomainInvitation>();
            for (DomainInvitation inv : invitations.values()) {
                if (domainId.equals(inv.getDomainId()) && inv.getStatus() == InvitationStatus.PENDING) {
                    result.add(inv);
                }
            }
            return result;
        }, executor);
    }
    
    @Override
    public CompletableFuture<List<DomainInvitation>> listMyInvitations() {
        return CompletableFuture.supplyAsync(() -> {
            List<DomainInvitation> result = new ArrayList<DomainInvitation>();
            for (DomainInvitation inv : invitations.values()) {
                if (inv.getStatus() == InvitationStatus.PENDING) {
                    result.add(inv);
                }
            }
            return result;
        }, executor);
    }
    
    @Override
    public void addDomainListener(DomainListener listener) {
        listeners.add(listener);
    }
    
    @Override
    public void removeDomainListener(DomainListener listener) {
        listeners.remove(listener);
    }
    
    private void notifyDomainCreated(DomainInfo domain) {
        for (DomainListener listener : listeners) {
            try {
                listener.onDomainCreated(domain);
            } catch (Exception e) {
                log.warn("Listener notification failed", e);
            }
        }
    }
    
    private void notifyDomainUpdated(DomainInfo domain) {
        for (DomainListener listener : listeners) {
            try {
                listener.onDomainUpdated(domain);
            } catch (Exception e) {
                log.warn("Listener notification failed", e);
            }
        }
    }
    
    private void notifyDomainDeleted(String domainId) {
        for (DomainListener listener : listeners) {
            try {
                listener.onDomainDeleted(domainId);
            } catch (Exception e) {
                log.warn("Listener notification failed", e);
            }
        }
    }
    
    private void notifyMemberAdded(String domainId, DomainMember member) {
        for (DomainListener listener : listeners) {
            try {
                listener.onMemberAdded(domainId, member);
            } catch (Exception e) {
                log.warn("Listener notification failed", e);
            }
        }
    }
    
    private void notifyMemberRemoved(String domainId, String memberId) {
        for (DomainListener listener : listeners) {
            try {
                listener.onMemberRemoved(domainId, memberId);
            } catch (Exception e) {
                log.warn("Listener notification failed", e);
            }
        }
    }
    
    private void notifyPolicyUpdated(String domainId, DomainPolicyConfig policy) {
        for (DomainListener listener : listeners) {
            try {
                listener.onPolicyUpdated(domainId, policy);
            } catch (Exception e) {
                log.warn("Listener notification failed", e);
            }
        }
    }
    
    public void shutdown() {
        log.info("Shutting down DomainManagementProtocol");
        executor.shutdown();
        domains.clear();
        domainMembers.clear();
        domainPolicies.clear();
        invitations.clear();
        log.info("DomainManagementProtocol shutdown complete");
    }
}
