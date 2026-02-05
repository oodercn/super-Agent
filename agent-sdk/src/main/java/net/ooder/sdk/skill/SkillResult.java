package net.ooder.sdk.skill;

import java.util.Map;

/**
 * 技能执行结果
 */
public class SkillResult {
    
    private final boolean success;
    private final Object data;
    private final String errorMessage;
    private final Map<String, Object> metadata;
    private final long executionTime;
    
    /**
     * 构建成功结果
     * @param data 结果数据
     * @param metadata 元数据
     * @return 技能执行结果
     */
    public static SkillResult success(Object data, Map<String, Object> metadata) {
        return new SkillResult(true, data, null, metadata);
    }
    
    /**
     * 构建失败结果
     * @param errorMessage 错误信息
     * @param metadata 元数据
     * @return 技能执行结果
     */
    public static SkillResult failure(String errorMessage, Map<String, Object> metadata) {
        return new SkillResult(false, null, errorMessage, metadata);
    }
    
    private SkillResult(boolean success, Object data, String errorMessage, Map<String, Object> metadata) {
        this.success = success;
        this.data = data;
        this.errorMessage = errorMessage;
        this.metadata = metadata;
        this.executionTime = System.currentTimeMillis();
    }
    
    /**
     * 是否执行成功
     * @return 执行结果
     */
    public boolean isSuccess() {
        return success;
    }
    
    /**
     * 获取结果数据
     * @return 结果数据
     */
    public Object getData() {
        return data;
    }
    
    /**
     * 获取错误信息
     * @return 错误信息
     */
    public String getErrorMessage() {
        return errorMessage;
    }
    
    /**
     * 获取元数据
     * @return 元数据
     */
    public Map<String, Object> getMetadata() {
        return metadata;
    }
    
    /**
     * 获取执行时间戳
     * @return 执行时间戳
     */
    public long getExecutionTime() {
        return executionTime;
    }
}