package net.ooder.nexus.service.share.impl;

import net.ooder.nexus.service.share.SkillShareService;
import net.ooder.sdk.api.skill.SkillPackageManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class SkillShareServiceImpl implements SkillShareService {

    private static final Logger log = LoggerFactory.getLogger(SkillShareServiceImpl.class);

    private final SkillPackageManager skillPackageManager;
    private final List<ShareEventListener> listeners = new CopyOnWriteArrayList<ShareEventListener>();
    private final Map<String, SharePrepareResultDTO> preparedShares = new ConcurrentHashMap<String, SharePrepareResultDTO>();
    private final Map<String, ShareInvitationDTO> pendingInvitations = new ConcurrentHashMap<String, ShareInvitationDTO>();
    private final Map<String, ShareProgressDTO> shareProgresses = new ConcurrentHashMap<String, ShareProgressDTO>();
    private final List<ShareRecordDTO> shareHistory = new ArrayList<ShareRecordDTO>();
    
    private String localNodeId;
    private String localNodeName;

    @Autowired
    public SkillShareServiceImpl(@Autowired(required = false) SkillPackageManager skillPackageManager) {
        this.skillPackageManager = skillPackageManager;
        this.localNodeId = UUID.randomUUID().toString();
        this.localNodeName = "Nexus-" + localNodeId.substring(0, 8);
        log.info("SkillShareService initialized with SDK 0.7.2");
    }

    @Override
    public CompletableFuture<SharePrepareResultDTO> prepareShare(String skillId, ShareConfigDTO config) {
        log.info("Preparing share for skill: {}", skillId);
        
        return CompletableFuture.supplyAsync(() -> {
            SharePrepareResultDTO result = new SharePrepareResultDTO();
            
            try {
                String shareId = UUID.randomUUID().toString();
                
                result.setShareId(shareId);
                result.setSkillId(skillId);
                result.setSkillName("Skill-" + skillId.substring(0, 8));
                result.setSkillVersion("1.0.0");
                result.setPackageSize(1024 * 1024);
                result.setChecksum(UUID.randomUUID().toString().substring(0, 16));
                result.setReady(true);
                
                preparedShares.put(shareId, result);
                
                log.info("Share prepared: {}", shareId);
                
            } catch (Exception e) {
                log.error("Failed to prepare share", e);
                result.setReady(false);
            }
            
            return result;
        });
    }

    @Override
    public CompletableFuture<Void> sendShareInvitation(String targetPeerId, String shareId) {
        log.info("Sending share invitation to: {} for share: {}", targetPeerId, shareId);
        
        return CompletableFuture.runAsync(() -> {
            SharePrepareResultDTO prepared = preparedShares.get(shareId);
            if (prepared == null) {
                throw new RuntimeException("Share not found: " + shareId);
            }
            
            ShareInvitationDTO invitation = new ShareInvitationDTO();
            invitation.setInvitationId(UUID.randomUUID().toString());
            invitation.setShareId(shareId);
            invitation.setSkillId(prepared.getSkillId());
            invitation.setSkillName(prepared.getSkillName());
            invitation.setSkillVersion(prepared.getSkillVersion());
            invitation.setSenderId(localNodeId);
            invitation.setSenderName(localNodeName);
            invitation.setPackageSize(prepared.getPackageSize());
            invitation.setCreatedAt(System.currentTimeMillis());
            invitation.setExpiresAt(System.currentTimeMillis() + 3600000);
            invitation.setStatus("PENDING");
            
            ShareProgressDTO progress = new ShareProgressDTO();
            progress.setShareId(shareId);
            progress.setStage("INVITATION_SENT");
            progress.setProgress(0);
            progress.setTotalBytes(prepared.getPackageSize());
            progress.setStatus("WAITING_ACCEPT");
            shareProgresses.put(shareId, progress);
            
            log.info("Share invitation sent: {}", invitation.getInvitationId());
        });
    }

    @Override
    public CompletableFuture<Void> acceptShareInvitation(String invitationId) {
        log.info("Accepting share invitation: {}", invitationId);
        
        return CompletableFuture.runAsync(() -> {
            ShareInvitationDTO invitation = pendingInvitations.get(invitationId);
            if (invitation == null) {
                throw new RuntimeException("Invitation not found: " + invitationId);
            }
            
            invitation.setStatus("ACCEPTED");
            
            ShareProgressDTO progress = new ShareProgressDTO();
            progress.setShareId(invitation.getShareId());
            progress.setStage("RECEIVING");
            progress.setProgress(0);
            progress.setTotalBytes(invitation.getPackageSize());
            progress.setStatus("TRANSFERRING");
            shareProgresses.put(invitation.getShareId(), progress);
            
            notifyShareStarted(invitation.getShareId());
            
            notifyInvitationAccepted(invitationId);
            
            log.info("Share invitation accepted: {}", invitationId);
        });
    }

    @Override
    public CompletableFuture<Void> declineShareInvitation(String invitationId) {
        log.info("Declining share invitation: {}", invitationId);
        
        return CompletableFuture.runAsync(() -> {
            ShareInvitationDTO invitation = pendingInvitations.remove(invitationId);
            if (invitation != null) {
                invitation.setStatus("DECLINED");
                notifyInvitationDeclined(invitationId);
            }
        });
    }

    @Override
    public CompletableFuture<List<ShareInvitationDTO>> getPendingShareInvitations() {
        log.info("Getting pending share invitations");
        
        return CompletableFuture.supplyAsync(() -> {
            return new ArrayList<ShareInvitationDTO>(pendingInvitations.values());
        });
    }

    @Override
    public CompletableFuture<ShareProgressDTO> getShareProgress(String shareId) {
        log.info("Getting share progress: {}", shareId);
        
        return CompletableFuture.supplyAsync(() -> {
            ShareProgressDTO progress = shareProgresses.get(shareId);
            if (progress == null) {
                progress = new ShareProgressDTO();
                progress.setShareId(shareId);
                progress.setStatus("NOT_FOUND");
            }
            return progress;
        });
    }

    @Override
    public CompletableFuture<Void> cancelShare(String shareId) {
        log.info("Cancelling share: {}", shareId);
        
        return CompletableFuture.runAsync(() -> {
            preparedShares.remove(shareId);
            shareProgresses.remove(shareId);
            
            log.info("Share cancelled: {}", shareId);
        });
    }

    @Override
    public CompletableFuture<List<ShareRecordDTO>> getShareHistory() {
        log.info("Getting share history");
        
        return CompletableFuture.supplyAsync(() -> {
            return new ArrayList<ShareRecordDTO>(shareHistory);
        });
    }

    @Override
    public void addShareListener(ShareEventListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeShareListener(ShareEventListener listener) {
        listeners.remove(listener);
    }

    public void receiveShareInvitation(ShareInvitationDTO invitation) {
        log.info("Received share invitation: {}", invitation.getInvitationId());
        
        pendingInvitations.put(invitation.getInvitationId(), invitation);
        notifyInvitationReceived(invitation);
    }

    public void updateShareProgress(String shareId, int progress, long bytesTransferred, long transferSpeed) {
        ShareProgressDTO progressDTO = shareProgresses.get(shareId);
        if (progressDTO != null) {
            progressDTO.setProgress(progress);
            progressDTO.setBytesTransferred(bytesTransferred);
            progressDTO.setTransferSpeed(transferSpeed);
            
            if (progress >= 100) {
                progressDTO.setStage("COMPLETED");
                progressDTO.setStatus("COMPLETED");
                
                ShareRecordDTO record = new ShareRecordDTO();
                record.setShareId(shareId);
                record.setSkillId(progressDTO.getShareId());
                record.setDirection("RECEIVE");
                record.setCompletedAt(System.currentTimeMillis());
                record.setStatus("COMPLETED");
                shareHistory.add(record);
                
                notifyShareCompleted(shareId);
            } else {
                notifyShareProgress(shareId, progressDTO);
            }
        }
    }

    private void notifyInvitationReceived(ShareInvitationDTO invitation) {
        for (ShareEventListener listener : listeners) {
            try {
                listener.onInvitationReceived(invitation);
            } catch (Exception e) {
                log.warn("Listener error: {}", e.getMessage());
            }
        }
    }

    private void notifyInvitationAccepted(String invitationId) {
        for (ShareEventListener listener : listeners) {
            try {
                listener.onInvitationAccepted(invitationId);
            } catch (Exception e) {
                log.warn("Listener error: {}", e.getMessage());
            }
        }
    }

    private void notifyInvitationDeclined(String invitationId) {
        for (ShareEventListener listener : listeners) {
            try {
                listener.onInvitationDeclined(invitationId);
            } catch (Exception e) {
                log.warn("Listener error: {}", e.getMessage());
            }
        }
    }

    private void notifyShareStarted(String shareId) {
        for (ShareEventListener listener : listeners) {
            try {
                listener.onShareStarted(shareId);
            } catch (Exception e) {
                log.warn("Listener error: {}", e.getMessage());
            }
        }
    }

    private void notifyShareProgress(String shareId, ShareProgressDTO progress) {
        for (ShareEventListener listener : listeners) {
            try {
                listener.onShareProgress(shareId, progress);
            } catch (Exception e) {
                log.warn("Listener error: {}", e.getMessage());
            }
        }
    }

    private void notifyShareCompleted(String shareId) {
        for (ShareEventListener listener : listeners) {
            try {
                listener.onShareCompleted(shareId);
            } catch (Exception e) {
                log.warn("Listener error: {}", e.getMessage());
            }
        }
    }
}
