package net.ooder.skillcenter.p2p;

/**
 * P2P节点类型枚举
 */
public enum NodeType {
    /**
     * 个人设备节点
     * 例如：个人电脑、笔记本电脑、平板电脑等
     */
    PERSONAL,
    
    /**
     * 家庭服务器节点
     * 例如：家庭NAS、智能路由器等
     */
    HOME_SERVER,
    
    /**
     * 物联网设备节点
     * 例如：智能电视、智能音箱、智能灯泡等
     */
    IOT_DEVICE,
    
    /**
     * 移动设备节点
     * 例如：智能手机、智能手表等
     */
    MOBILE_DEVICE,
    
    /**
     * 组织服务器节点
     * 例如：企业服务器、团队协作服务器等
     */
    ORGANIZATION_SERVER,
    
    /**
     * 公共服务节点
     * 例如：公共API服务器、云服务节点等
     */
    PUBLIC_SERVICE
}