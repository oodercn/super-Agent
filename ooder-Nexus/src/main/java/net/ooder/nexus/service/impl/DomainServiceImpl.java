package net.ooder.nexus.service.impl;

import net.ooder.nexus.service.DomainService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
public class DomainServiceImpl implements DomainService {

    private static final Logger log = LoggerFactory.getLogger(DomainServiceImpl.class);

    private final List<DomainInfo> joinedDomains = new ArrayList<DomainInfo>();
    private DomainInfo currentDomain = null;

    public DomainServiceImpl() {
        log.info("DomainServiceImpl initialized with SDK 0.7.2");
    }

    @Override
    public CompletableFuture<DomainInvitation> parseInvitation(String inviteCode) {
        log.info("Parsing invitation: {}", inviteCode);
        
        return CompletableFuture.supplyAsync(() -> {
            DomainInvitation invitation = new DomainInvitation();
            invitation.setInviteCode(inviteCode);
            invitation.setDomainId("domain-" + inviteCode.hashCode());
            invitation.setDomainName("示例组织域");
            invitation.setInviterId("admin-001");
            invitation.setInviterName("管理员");
            invitation.setExpireTime(System.currentTimeMillis() + 24 * 60 * 60 * 1000);
            invitation.setRole("MEMBER");
            return invitation;
        });
    }

    @Override
    public CompletableFuture<DomainMembership> acceptInvitation(String inviteCode) {
        log.info("Accepting invitation: {}", inviteCode);
        
        return CompletableFuture.supplyAsync(() -> {
            DomainInvitation invitation = parseInvitation(inviteCode).join();
            
            DomainMembership membership = new DomainMembership();
            membership.setMembershipId("membership-" + System.currentTimeMillis());
            membership.setDomainId(invitation.getDomainId());
            membership.setMemberId("nexus-001");
            membership.setRole(invitation.getRole());
            membership.setJoinTime(System.currentTimeMillis());
            
            DomainInfo domainInfo = new DomainInfo();
            domainInfo.setDomainId(invitation.getDomainId());
            domainInfo.setDomainName(invitation.getDomainName());
            domainInfo.setOwnerId(invitation.getInviterId());
            domainInfo.setMemberCount(1);
            domainInfo.setCreateTime(System.currentTimeMillis());
            
            joinedDomains.add(domainInfo);
            currentDomain = domainInfo;
            
            log.info("Successfully joined domain: {}", invitation.getDomainName());
            return membership;
        });
    }

    @Override
    public CompletableFuture<Void> rejectInvitation(String inviteCode) {
        log.info("Rejecting invitation: {}", inviteCode);
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public CompletableFuture<List<DomainInfo>> listDomains() {
        log.info("Listing joined domains");
        return CompletableFuture.completedFuture(new ArrayList<DomainInfo>(joinedDomains));
    }

    @Override
    public CompletableFuture<DomainInfo> getDomainInfo(String domainId) {
        log.info("Getting domain info: {}", domainId);
        
        return CompletableFuture.supplyAsync(() -> {
            for (DomainInfo domain : joinedDomains) {
                if (domain.getDomainId().equals(domainId)) {
                    return domain;
                }
            }
            return null;
        });
    }

    @Override
    public CompletableFuture<Void> leaveDomain(String domainId) {
        log.info("Leaving domain: {}", domainId);
        
        return CompletableFuture.runAsync(() -> {
            joinedDomains.removeIf(d -> d.getDomainId().equals(domainId));
            if (currentDomain != null && currentDomain.getDomainId().equals(domainId)) {
                currentDomain = joinedDomains.isEmpty() ? null : joinedDomains.get(0);
            }
            log.info("Successfully left domain: {}", domainId);
        });
    }

    @Override
    public CompletableFuture<List<DomainMember>> listDomainMembers(String domainId) {
        log.info("Listing members of domain: {}", domainId);
        
        return CompletableFuture.supplyAsync(() -> {
            List<DomainMember> members = new ArrayList<DomainMember>();
            
            DomainMember member1 = new DomainMember();
            member1.setMemberId("admin-001");
            member1.setMemberName("管理员");
            member1.setRole("ADMIN");
            member1.setStatus("ONLINE");
            member1.setJoinTime(System.currentTimeMillis() - 30 * 24 * 60 * 60 * 1000L);
            members.add(member1);
            
            DomainMember member2 = new DomainMember();
            member2.setMemberId("nexus-001");
            member2.setMemberName("我的Nexus");
            member2.setRole("MEMBER");
            member2.setStatus("ONLINE");
            member2.setJoinTime(System.currentTimeMillis());
            members.add(member2);
            
            return members;
        });
    }

    @Override
    public CompletableFuture<List<DomainResource>> listDomainResources(String domainId) {
        log.info("Listing resources of domain: {}", domainId);
        
        return CompletableFuture.supplyAsync(() -> {
            List<DomainResource> resources = new ArrayList<DomainResource>();
            
            DomainResource res1 = new DomainResource();
            res1.setResourceId("skill-data-analysis");
            res1.setResourceName("数据分析技能");
            res1.setResourceType("SKILL");
            res1.setDescription("提供数据分析能力");
            res1.setAccessLevel("READ");
            resources.add(res1);
            
            DomainResource res2 = new DomainResource();
            res2.setResourceId("storage-shared");
            res2.setResourceName("共享存储");
            res2.setResourceType("STORAGE");
            res2.setDescription("域共享存储空间");
            res2.setAccessLevel("READ_WRITE");
            resources.add(res2);
            
            return resources;
        });
    }

    @Override
    public CompletableFuture<ResourceAccessToken> requestResourceAccess(String domainId, String resourceId) {
        log.info("Requesting access to resource: {} in domain: {}", resourceId, domainId);
        
        return CompletableFuture.supplyAsync(() -> {
            ResourceAccessToken token = new ResourceAccessToken();
            token.setToken("token-" + System.currentTimeMillis());
            token.setResourceId(resourceId);
            token.setExpireTime(System.currentTimeMillis() + 3600 * 1000);
            token.setAccessLevel("READ");
            return token;
        });
    }

    @Override
    public boolean hasJoinedDomain() {
        return !joinedDomains.isEmpty();
    }

    @Override
    public DomainInfo getCurrentDomain() {
        return currentDomain;
    }
}
