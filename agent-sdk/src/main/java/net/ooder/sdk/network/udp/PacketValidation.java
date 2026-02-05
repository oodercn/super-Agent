package net.ooder.sdk.network.udp;

/**
 * UDP数据包验证接口
 */
public interface PacketValidation {
    /**
     * 验证数据包
     * @param packet 数据包
     * @return 是否有效
     */
    boolean validate(Object packet);
    
    /**
     * 获取验证错误信息
     * @return 错误信息
     */
    String getErrorMessage();
    
    /**
     * 检查是否启用验证
     * @return 是否启用
     */
    boolean isEnabled();
}
