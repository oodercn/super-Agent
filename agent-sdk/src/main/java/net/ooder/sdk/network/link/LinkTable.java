package net.ooder.sdk.network.link;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LinkTable {
    private final Map<String, LinkInfo> links;
    
    public LinkTable() {
        this.links = new ConcurrentHashMap<>();
    }
    
    /**
     * 添加或更新链路信息
     */
    public void addOrUpdateLink(LinkInfo linkInfo) {
        links.put(linkInfo.getLinkId(), linkInfo);
    }
    
    /**
     * 删除链路信息
     */
    public void removeLink(String linkId) {
        links.remove(linkId);
    }
    
    /**
     * 获取链路信息
     */
    public LinkInfo getLink(String linkId) {
        return links.get(linkId);
    }
    
    /**
     * 获取所有链路信息
     */
    public Map<String, LinkInfo> getAllLinks() {
        return links;
    }
    
    /**
     * 获取链路表大小
     */
    public int size() {
        return links.size();
    }
    
    /**
     * 清空链路表
     */
    public void clear() {
        links.clear();
    }
    
    /**
     * 检查链路是否存在
     */
    public boolean containsLink(String linkId) {
        return links.containsKey(linkId);
    }
}
