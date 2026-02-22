package net.ooder.skillcenter.sdk.protocol;

import net.ooder.nexus.skillcenter.dto.protocol.DomainDTO;
import net.ooder.nexus.skillcenter.dto.protocol.DomainDTO.AddMemberRequestDTO;
import net.ooder.nexus.skillcenter.dto.protocol.DomainDTO.CreateDomainRequestDTO;
import net.ooder.nexus.skillcenter.dto.protocol.DomainDTO.DomainInfoDTO;
import net.ooder.nexus.skillcenter.dto.protocol.DomainDTO.DomainInvitationDTO;
import net.ooder.nexus.skillcenter.dto.protocol.DomainDTO.DomainMemberDTO;
import net.ooder.nexus.skillcenter.dto.protocol.DomainDTO.DomainPolicyConfigDTO;
import net.ooder.nexus.skillcenter.dto.protocol.DomainDTO.DomainQueryDTO;
import net.ooder.nexus.skillcenter.dto.protocol.DomainDTO.InvitationRequestDTO;
import net.ooder.nexus.skillcenter.dto.protocol.DomainDTO.UpdateDomainRequestDTO;
import net.ooder.skillcenter.dto.PageResult;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface DomainManagementProtocolAdapter {

    CompletableFuture<DomainInfoDTO> createDomain(CreateDomainRequestDTO request);

    CompletableFuture<Void> deleteDomain(String domainId);

    CompletableFuture<DomainInfoDTO> getDomain(String domainId);

    CompletableFuture<PageResult<DomainInfoDTO>> listDomains(DomainQueryDTO query);

    CompletableFuture<Void> updateDomain(String domainId, UpdateDomainRequestDTO request);

    CompletableFuture<Void> addDomainMember(String domainId, AddMemberRequestDTO request);

    CompletableFuture<Void> removeDomainMember(String domainId, String memberId);

    CompletableFuture<List<DomainMemberDTO>> listDomainMembers(String domainId);

    CompletableFuture<Void> setDomainPolicy(String domainId, DomainPolicyConfigDTO policy);

    CompletableFuture<DomainPolicyConfigDTO> getDomainPolicy(String domainId);

    CompletableFuture<Void> inviteToDomain(String domainId, InvitationRequestDTO request);

    CompletableFuture<List<DomainInvitationDTO>> listPendingInvitations(String domainId);

    CompletableFuture<Void> revokeInvitation(String invitationId);

    void addDomainListener(DomainEventListener listener);

    void removeDomainListener(DomainEventListener listener);

    boolean isAvailable();

    interface DomainEventListener {
        void onDomainCreated(DomainInfoDTO domain);
        void onDomainUpdated(DomainInfoDTO domain);
        void onDomainDeleted(String domainId);
        void onMemberAdded(String domainId, DomainMemberDTO member);
        void onMemberRemoved(String domainId, String memberId);
        void onPolicyUpdated(String domainId, DomainPolicyConfigDTO policy);
    }
}
