package net.ooder.sdk.network.udp;

/**
 * 默认的UDP数据包验证实现
 */
public class DefaultPacketValidation implements PacketValidation {
    private String errorMessage;
    private boolean enabled = true;

    @Override
    public boolean validate(Object packet) {
        // 简单的验证逻辑
        if (packet == null) {
            errorMessage = "Packet cannot be null";
            return false;
        }
        return true;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
    
    /**
     * 设置是否启用
     * @param enabled 是否启用
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}