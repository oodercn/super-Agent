package net.ooder.sdk.api.share.impl;

import net.ooder.sdk.api.share.SkillShareService;
import net.ooder.sdk.api.share.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;

public class SkillShareServiceImpl implements SkillShareService {
    
    private static final Logger log = LoggerFactory.getLogger(SkillShareServiceImpl.class);
    
    private final Map<String, SharePrepareResult> preparedShares;
    private final Map<String, ShareInvitation> pendingInvitations;
    private final Map<String, ShareProgress> shareProgresses;
    private final List<ShareRecord> shareHistory;
    private final List<ShareEventListener> listeners;
    private final ExecutorService executor;
    
    public SkillShareServiceImpl() {
        this.preparedShares = new ConcurrentHashMap<String, SharePrepareResult>();
        this.pendingInvitations = new ConcurrentHashMap<String, ShareInvitation>();
        this.shareProgresses = new ConcurrentHashMap<String, ShareProgress>();
        this.shareHistory = new CopyOnWriteArrayList<ShareRecord>();
        this.listeners = new CopyOnWriteArrayList<ShareEventListener>();
        this.executor = Executors.newCachedThreadPool();
        log.info("SkillShareServiceImpl initialized");
    }
    
    @Override
    public CompletableFuture<SharePrepareResult> prepareShare(String skillId, ShareConfig config) {
        return CompletableFuture.supplyAsync(() -> {
            log.info("Preparing share for skill: {}", skillId);
            
            SharePrepareResult result = new SharePrepareResult();
            result.setShareId("share-" + UUID.randomUUID().toString().substring(0, 8));
            result.setSkillId(skillId);
            result.setSkillName(config.getSkillName() != null ? config.getSkillName() : "Skill-" + skillId);
            result.setVersion(config.getVersion() != null ? config.getVersion() : "1.0.0");
            result.setPackageSize((long) (Math.random() * 1024 * 1024 * 10));
            result.setPackageHash(UUID.randomUUID().toString().substring(0, 16));
            result.setPreparedAt(System.currentTimeMillis());
            result.setExpiresAt(System.currentTimeMillis() + 3600000);
            result.setSuccess(true);
            
            preparedShares.put(result.getShareId(), result);
            
            ShareProgress progress = new ShareProgress();
            progress.setShareId(result.getShareId());
            progress.setStatus("PREPARED");
            progress.setProgress(0);
            progress.setTotalBytes(result.getPackageSize());
            progress.setStartTime(System.currentTimeMillis());
            shareProgresses.put(result.getShareId(), progress);
            
            notifySharePrepared(result);
            log.info("Share prepared: shareId={}", result.getShareId());
            return result;
        }, executor);
    }
    
    @Override
    public CompletableFuture<Void> sendShareInvitation(String targetPeerId, String shareId) {
        return CompletableFuture.runAsync(() -> {
            log.info("Sending share invitation: shareId={}, target={}", shareId, targetPeerId);
            
            SharePrepareResult prepared = preparedShares.get(shareId);
            if (prepared == null) {
                throw new RuntimeException("Share not found: " + shareId);
            }
            
            ShareInvitation invitation = new ShareInvitation();
            invitation.setInvitationId("inv-" + UUID.randomUUID().toString().substring(0, 8));
            invitation.setShareId(shareId);
            invitation.setSkillId(prepared.getSkillId());
            invitation.setSkillName(prepared.getSkillName());
            invitation.setVersion(prepared.getVersion());
            invitation.setSenderId("local-node");
            invitation.setSenderName("Local Node");
            invitation.setTargetId(targetPeerId);
            invitation.setCreatedAt(System.currentTimeMillis());
            invitation.setExpiresAt(prepared.getExpiresAt());
            invitation.setStatus(ShareInvitationStatus.PENDING);
            invitation.setPackageSize(prepared.getPackageSize());
            
            pendingInvitations.put(invitation.getInvitationId(), invitation);
            
            notifyShareInvitationSent(shareId, targetPeerId);
            log.info("Share invitation sent: invitationId={}", invitation.getInvitationId());
        }, executor);
    }
    
    @Override
    public CompletableFuture<Void> acceptShareInvitation(String invitationId) {
        return CompletableFuture.runAsync(() -> {
            log.info("Accepting share invitation: {}", invitationId);
            
            ShareInvitation invitation = pendingInvitations.get(invitationId);
            if (invitation == null) {
                throw new RuntimeException("Invitation not found: " + invitationId);
            }
            
            invitation.setStatus(ShareInvitationStatus.ACCEPTED);
            
            ShareProgress progress = shareProgresses.get(invitation.getShareId());
            if (progress != null) {
                progress.setStatus("TRANSFERRING");
                simulateTransfer(invitation.getShareId());
            }
            
            ShareRecord record = new ShareRecord();
            record.setRecordId("rec-" + UUID.randomUUID().toString().substring(0, 8));
            record.setShareId(invitation.getShareId());
            record.setSkillId(invitation.getSkillId());
            record.setSkillName(invitation.getSkillName());
            record.setVersion(invitation.getVersion());
            record.setSenderId(invitation.getSenderId());
            record.setReceiverId("local-node");
            record.setDirection(ShareDirection.RECEIVED);
            record.setCompletedAt(System.currentTimeMillis());
            record.setSuccess(true);
            shareHistory.add(record);
            
            notifyShareInvitationAccepted(invitationId);
            log.info("Share invitation accepted: {}", invitationId);
        }, executor);
    }
    
    @Override
    public CompletableFuture<Void> declineShareInvitation(String invitationId) {
        return CompletableFuture.runAsync(() -> {
            log.info("Declining share invitation: {}", invitationId);
            
            ShareInvitation invitation = pendingInvitations.remove(invitationId);
            if (invitation != null) {
                invitation.setStatus(ShareInvitationStatus.DECLINED);
                notifyShareInvitationDeclined(invitationId);
                log.info("Share invitation declined: {}", invitationId);
            }
        }, executor);
    }
    
    @Override
    public CompletableFuture<List<ShareInvitation>> getPendingShareInvitations() {
        return CompletableFuture.supplyAsync(() -> {
            List<ShareInvitation> result = new ArrayList<ShareInvitation>();
            for (ShareInvitation invitation : pendingInvitations.values()) {
                if (invitation.getStatus() == ShareInvitationStatus.PENDING) {
                    result.add(invitation);
                }
            }
            return result;
        }, executor);
    }
    
    @Override
    public CompletableFuture<ShareProgress> getShareProgress(String shareId) {
        return CompletableFuture.supplyAsync(() -> shareProgresses.get(shareId), executor);
    }
    
    @Override
    public CompletableFuture<Void> cancelShare(String shareId) {
        return CompletableFuture.runAsync(() -> {
            log.info("Cancelling share: {}", shareId);
            
            preparedShares.remove(shareId);
            shareProgresses.remove(shareId);
            
            Iterator<ShareInvitation> it = pendingInvitations.values().iterator();
            while (it.hasNext()) {
                ShareInvitation inv = it.next();
                if (inv.getShareId().equals(shareId)) {
                    inv.setStatus(ShareInvitationStatus.CANCELLED);
                    it.remove();
                }
            }
            
            notifyShareCancelled(shareId);
            log.info("Share cancelled: {}", shareId);
        }, executor);
    }
    
    @Override
    public CompletableFuture<List<ShareRecord>> getShareHistory() {
        return CompletableFuture.supplyAsync(() -> new ArrayList<ShareRecord>(shareHistory), executor);
    }
    
    @Override
    public void addShareListener(ShareEventListener listener) {
        listeners.add(listener);
    }
    
    @Override
    public void removeShareListener(ShareEventListener listener) {
        listeners.remove(listener);
    }
    
    private void simulateTransfer(String shareId) {
        executor.submit(new Runnable() {
            @Override
            public void run() {
                ShareProgress progress = shareProgresses.get(shareId);
                if (progress == null) return;
                
                for (int i = 0; i <= 100; i += 10) {
                    try {
                        Thread.sleep(100);
                        progress.setProgress(i);
                        progress.setBytesTransferred((long) (progress.getTotalBytes() * i / 100.0));
                        progress.setCurrentPhase(i < 50 ? "UPLOADING" : "INSTALLING");
                        notifyShareProgress(shareId, progress);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
                
                progress.setStatus("COMPLETED");
                progress.setProgress(100);
                notifyShareCompleted(shareId, true);
            }
        });
    }
    
    private void notifySharePrepared(SharePrepareResult result) {
        for (ShareEventListener listener : listeners) {
            try {
                listener.onSharePrepared(result);
            } catch (Exception e) {
                log.warn("Listener notification failed", e);
            }
        }
    }
    
    private void notifyShareInvitationSent(String shareId, String targetPeerId) {
        for (ShareEventListener listener : listeners) {
            try {
                listener.onShareInvitationSent(shareId, targetPeerId);
            } catch (Exception e) {
                log.warn("Listener notification failed", e);
            }
        }
    }
    
    private void notifyShareInvitationAccepted(String invitationId) {
        for (ShareEventListener listener : listeners) {
            try {
                listener.onShareInvitationAccepted(invitationId);
            } catch (Exception e) {
                log.warn("Listener notification failed", e);
            }
        }
    }
    
    private void notifyShareInvitationDeclined(String invitationId) {
        for (ShareEventListener listener : listeners) {
            try {
                listener.onShareInvitationDeclined(invitationId);
            } catch (Exception e) {
                log.warn("Listener notification failed", e);
            }
        }
    }
    
    private void notifyShareProgress(String shareId, ShareProgress progress) {
        for (ShareEventListener listener : listeners) {
            try {
                listener.onShareProgress(shareId, progress);
            } catch (Exception e) {
                log.warn("Listener notification failed", e);
            }
        }
    }
    
    private void notifyShareCompleted(String shareId, boolean success) {
        for (ShareEventListener listener : listeners) {
            try {
                listener.onShareCompleted(shareId, success);
            } catch (Exception e) {
                log.warn("Listener notification failed", e);
            }
        }
    }
    
    private void notifyShareCancelled(String shareId) {
        for (ShareEventListener listener : listeners) {
            try {
                listener.onShareCancelled(shareId);
            } catch (Exception e) {
                log.warn("Listener notification failed", e);
            }
        }
    }
    
    public void addReceivedInvitation(ShareInvitation invitation) {
        pendingInvitations.put(invitation.getInvitationId(), invitation);
        for (ShareEventListener listener : listeners) {
            try {
                listener.onShareInvitationReceived(invitation);
            } catch (Exception e) {
                log.warn("Listener notification failed", e);
            }
        }
    }
    
    public void shutdown() {
        log.info("Shutting down SkillShareService");
        executor.shutdown();
        preparedShares.clear();
        pendingInvitations.clear();
        shareProgresses.clear();
        shareHistory.clear();
        log.info("SkillShareService shutdown complete");
    }
}
