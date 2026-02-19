package net.ooder.sdk.api.network;

/**
 * Link Listener Interface
 *
 * @author ooder Team
 * @since 0.7.1
 */
public interface LinkListener {

    /**
     * Called when a link is created
     *
     * @param link the created link
     */
    void onLinkCreated(LinkInfo link);

    /**
     * Called when a link is removed
     *
     * @param linkId the removed link ID
     */
    void onLinkRemoved(String linkId);

    /**
     * Called when link quality changes
     *
     * @param linkId the link ID
     * @param quality the new quality info
     */
    void onQualityChanged(String linkId, LinkQualityInfo quality);

    /**
     * Called when a link status changes
     *
     * @param linkId the link ID
     * @param status the new status
     */
    void onStatusChanged(String linkId, String status);
}
