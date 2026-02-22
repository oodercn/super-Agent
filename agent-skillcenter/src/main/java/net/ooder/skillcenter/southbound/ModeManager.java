package net.ooder.skillcenter.southbound;

import java.util.concurrent.CompletableFuture;

/**
 * 模式管理器接口
 * 
 * 管理南向独立模式与北向协作模式之间的切换
 */
public interface ModeManager {

    CompletableFuture<Boolean> checkNorthboundAvailable();

    void switchToIndependentMode();

    void switchToCollaborativeMode(String skillCenterEndpoint);

    OperationMode getCurrentMode();
}
