package net.ooder.sdk.api.share.model;

public interface ShareEventListener {
    
    void onSharePrepared(SharePrepareResult result);
    
    void onShareInvitationSent(String shareId, String targetPeerId);
    
    void onShareInvitationReceived(ShareInvitation invitation);
    
    void onShareInvitationAccepted(String invitationId);
    
    void onShareInvitationDeclined(String invitationId);
    
    void onShareProgress(String shareId, ShareProgress progress);
    
    void onShareCompleted(String shareId, boolean success);
    
    void onShareCancelled(String shareId);
    
    void onShareError(String shareId, String errorMessage);
}
