package net.ooder.sdk.llm;

import java.util.concurrent.CompletableFuture;

public interface LlmService {
    /**
     * 发送代码生成请求到LLM Service
     */
    CompletableFuture<LlmResponse> generateCode(LlmRequest request);

    /**
     * 初始化LLM Service配置
     */
    void init(LlmConfig config);

    /**
     * 检查LLM Service连接状态
     */
    boolean isConnected();

    /**
     * 关闭LLM Service连接
     */
    void close();
}