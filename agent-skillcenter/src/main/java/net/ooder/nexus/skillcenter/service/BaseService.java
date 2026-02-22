package net.ooder.nexus.skillcenter.service;

/**
 * 基础Service接口 - 符合 ooderNexus 规范
 * 为所有Service提供统一的抽象
 */
public interface BaseService {

    /**
     * 初始化服务
     */
    void initialize();

    /**
     * 检查服务状态
     * @return 服务状态
     */
    boolean isServiceAvailable();

    /**
     * 获取服务名称
     * @return 服务名称
     */
    String getServiceName();

    /**
     * 获取服务版本
     * @return 服务版本
     */
    String getServiceVersion();
}