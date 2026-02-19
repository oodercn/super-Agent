package net.ooder.sdk.api.share;

import net.ooder.sdk.api.share.model.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface SkillShareService {
    
    CompletableFuture<SharePrepareResult> prepareShare(String skillId, ShareConfig config);
    
    CompletableFuture<Void> sendShareInvitation(String targetPeerId, String shareId);
    
    CompletableFuture<Void> acceptShareInvitation(String invitationId);
    
    CompletableFuture<Void> declineShareInvitation(String invitationId);
    
    CompletableFuture<List<ShareInvitation>> getPendingShareInvitations();
    
    CompletableFuture<ShareProgress> getShareProgress(String shareId);
    
    CompletableFuture<Void> cancelShare(String shareId);
    
    CompletableFuture<List<ShareRecord>> getShareHistory();
    
    void addShareListener(ShareEventListener listener);
    
    void removeShareListener(ShareEventListener listener);
}
