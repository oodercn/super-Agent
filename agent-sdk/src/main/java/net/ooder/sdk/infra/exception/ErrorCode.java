
package net.ooder.sdk.infra.exception;

public enum ErrorCode {
    
    SUCCESS(0, "Success"),
    
    UNKNOWN_ERROR(1000, "Unknown error"),
    INTERNAL_ERROR(1001, "Internal error"),
    PARAMETER_ERROR(1002, "Parameter error"),
    TIMEOUT_ERROR(1003, "Timeout error"),
    PERMISSION_DENIED(1004, "Permission denied"),
    RESOURCE_NOT_FOUND(1005, "Resource not found"),
    RESOURCE_ALREADY_EXISTS(1006, "Resource already exists"),
    RESOURCE_EXHAUSTED(1007, "Resource exhausted"),
    
    NETWORK_ERROR(2000, "Network error"),
    NETWORK_TIMEOUT(2001, "Network timeout"),
    NETWORK_UNREACHABLE(2002, "Network unreachable"),
    CONNECTION_REFUSED(2003, "Connection refused"),
    CONNECTION_RESET(2004, "Connection reset"),
    
    SKILL_ERROR(3000, "Skill error"),
    SKILL_NOT_FOUND(3001, "Skill not found"),
    SKILL_ALREADY_INSTALLED(3002, "Skill already installed"),
    SKILL_INSTALL_FAILED(3003, "Skill installation failed"),
    SKILL_UNINSTALL_FAILED(3004, "Skill uninstallation failed"),
    SKILL_START_FAILED(3005, "Skill start failed"),
    SKILL_STOP_FAILED(3006, "Skill stop failed"),
    SKILL_INVOKE_FAILED(3007, "Skill invocation failed"),
    SKILL_DEPENDENCY_ERROR(3008, "Skill dependency error"),
    
    SCENE_ERROR(4000, "Scene error"),
    SCENE_NOT_FOUND(4001, "Scene not found"),
    SCENE_ALREADY_EXISTS(4002, "Scene already exists"),
    SCENE_JOIN_FAILED(4003, "Scene join failed"),
    SCENE_LEAVE_FAILED(4004, "Scene leave failed"),
    SCENE_ACTIVATE_FAILED(4005, "Scene activation failed"),
    SCENE_DEACTIVATE_FAILED(4006, "Scene deactivation failed"),
    
    SCENE_GROUP_ERROR(5000, "Scene group error"),
    SCENE_GROUP_NOT_FOUND(5001, "Scene group not found"),
    SCENE_GROUP_ALREADY_EXISTS(5002, "Scene group already exists"),
    SCENE_GROUP_CREATE_FAILED(5003, "Scene group creation failed"),
    SCENE_GROUP_JOIN_FAILED(5004, "Scene group join failed"),
    SCENE_GROUP_LEAVE_FAILED(5005, "Scene group leave failed"),
    
    FAILOVER_ERROR(6000, "Failover error"),
    FAILOVER_NO_BACKUP_AVAILABLE(6001, "No backup agent available for failover"),
    FAILOVER_EVALUATION_FAILED(6002, "Failover evaluation failed"),
    FAILOVER_ROLE_CHANGE_FAILED(6003, "Role change failed during failover"),
    FAILOVER_KEY_RECONSTRUCTION_FAILED(6004, "Key reconstruction failed during failover"),
    
    VFS_ERROR(7000, "VFS error"),
    VFS_NOT_FOUND(7001, "VFS not found"),
    VFS_ACCESS_DENIED(7002, "VFS access denied"),
    VFS_SYNC_FAILED(7003, "VFS sync failed"),
    VFS_OPERATION_FAILED(7004, "VFS operation failed"),
    
    AUTH_ERROR(8000, "Authentication error"),
    AUTH_FAILED(8001, "Authentication failed"),
    AUTH_TOKEN_EXPIRED(8002, "Authentication token expired"),
    AUTH_TOKEN_INVALID(8003, "Authentication token invalid"),
    
    DISCOVERY_ERROR(9000, "Discovery error"),
    DISCOVERY_TIMEOUT(9001, "Discovery timeout"),
    DISCOVERY_NO_RESULTS(9002, "Discovery returned no results"),
    DISCOVERY_METHOD_NOT_SUPPORTED(9003, "Discovery method not supported");
    
    private final int code;
    private final String message;
    
    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
    
    public int getCode() {
        return code;
    }
    
    public String getMessage() {
        return message;
    }
    
    public String getCodeString() {
        return String.format("E%04d", code);
    }
    
    @Override
    public String toString() {
        return getCodeString() + ": " + message;
    }
    
    public static ErrorCode fromCode(int code) {
        for (ErrorCode errorCode : values()) {
            if (errorCode.code == code) {
                return errorCode;
            }
        }
        return UNKNOWN_ERROR;
    }
    
    public boolean isRetryable() {
        return this == NETWORK_ERROR 
            || this == NETWORK_TIMEOUT
            || this == TIMEOUT_ERROR
            || this == DISCOVERY_TIMEOUT;
    }
    
    public boolean isClientError() {
        return code >= 1000 && code < 2000;
    }
    
    public boolean isNetworkError() {
        return code >= 2000 && code < 3000;
    }
    
    public boolean isSkillError() {
        return code >= 3000 && code < 4000;
    }
    
    public boolean isSceneError() {
        return code >= 4000 && code < 6000;
    }
    
    public boolean isVfsError() {
        return code >= 7000 && code < 8000;
    }
    
    public boolean isAuthError() {
        return code >= 8000 && code < 9000;
    }
}
