package net.ooder.sdk.api.scene.store;

import java.util.List;

public interface LinkStore {
    
    void saveLink(LinkConfig link);
    
    LinkConfig loadLink(String linkId);
    
    void deleteLink(String linkId);
    
    List<LinkConfig> listLinks(String sceneId);
    
    List<LinkConfig> listAllLinks();
    
    List<LinkConfig> getLinksBySource(String sourceId);
    
    List<LinkConfig> getLinksByTarget(String targetId);
    
    List<LinkConfig> getLinksByType(String sceneId, String linkType);
    
    boolean linkExists(String linkId);
    
    void updateLinkStatus(String linkId, String status);
}
