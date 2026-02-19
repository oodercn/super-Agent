
package net.ooder.sdk.common.constants;

public final class ErrorCodes {
    
    private ErrorCodes() {}
    
    public static final String SUCCESS = "SUCCESS";
    public static final String UNKNOWN_ERROR = "UNKNOWN_ERROR";
    
    public static final String AGENT_NOT_FOUND = "AGENT_NOT_FOUND";
    public static final String AGENT_ALREADY_EXISTS = "AGENT_ALREADY_EXISTS";
    public static final String AGENT_NOT_ACTIVE = "AGENT_NOT_ACTIVE";
    public static final String AGENT_TYPE_INVALID = "AGENT_TYPE_INVALID";
    
    public static final String SKILL_NOT_FOUND = "SKILL_NOT_FOUND";
    public static final String SKILL_ALREADY_INSTALLED = "SKILL_ALREADY_INSTALLED";
    public static final String SKILL_INSTALL_FAILED = "SKILL_INSTALL_FAILED";
    public static final String SKILL_UNINSTALL_FAILED = "SKILL_UNINSTALL_FAILED";
    public static final String SKILL_DEPENDENCY_ERROR = "SKILL_DEPENDENCY_ERROR";
    public static final String SKILL_VERSION_MISMATCH = "SKILL_VERSION_MISMATCH";
    
    public static final String SCENE_NOT_FOUND = "SCENE_NOT_FOUND";
    public static final String SCENE_ALREADY_EXISTS = "SCENE_ALREADY_EXISTS";
    public static final String SCENE_MEMBER_NOT_FOUND = "SCENE_MEMBER_NOT_FOUND";
    public static final String SCENE_GROUP_ERROR = "SCENE_GROUP_ERROR";
    public static final String SCENE_FAILOVER_ERROR = "SCENE_FAILOVER_ERROR";
    
    public static final String CAPABILITY_NOT_FOUND = "CAPABILITY_NOT_FOUND";
    public static final String CAPABILITY_NOT_AVAILABLE = "CAPABILITY_NOT_AVAILABLE";
    public static final String CAPABILITY_INVOKE_ERROR = "CAPABILITY_INVOKE_ERROR";
    
    public static final String AUTH_FAILED = "AUTH_FAILED";
    public static final String AUTH_TOKEN_EXPIRED = "AUTH_TOKEN_EXPIRED";
    public static final String AUTH_TOKEN_INVALID = "AUTH_TOKEN_INVALID";
    public static final String PERMISSION_DENIED = "PERMISSION_DENIED";
    
    public static final String NETWORK_ERROR = "NETWORK_ERROR";
    public static final String NETWORK_TIMEOUT = "NETWORK_TIMEOUT";
    public static final String NETWORK_CONNECTION_FAILED = "NETWORK_CONNECTION_FAILED";
    
    public static final String STORAGE_ERROR = "STORAGE_ERROR";
    public static final String STORAGE_NOT_FOUND = "STORAGE_NOT_FOUND";
    public static final String STORAGE_WRITE_ERROR = "STORAGE_WRITE_ERROR";
    public static final String STORAGE_READ_ERROR = "STORAGE_READ_ERROR";
    
    public static final String CONFIG_ERROR = "CONFIG_ERROR";
    public static final String CONFIG_NOT_FOUND = "CONFIG_NOT_FOUND";
    public static final String CONFIG_INVALID = "CONFIG_INVALID";
    
    public static final String VALIDATION_ERROR = "VALIDATION_ERROR";
    public static final String INVALID_PARAMETER = "INVALID_PARAMETER";
    public static final String MISSING_PARAMETER = "MISSING_PARAMETER";
    
    public static final String INTERNAL_ERROR = "INTERNAL_ERROR";
    public static final String NOT_IMPLEMENTED = "NOT_IMPLEMENTED";
    public static final String SERVICE_UNAVAILABLE = "SERVICE_UNAVAILABLE";
    
    public static String getMessage(String code) {
        switch (code) {
            case SUCCESS: return "Operation successful";
            case UNKNOWN_ERROR: return "Unknown error occurred";
            case AGENT_NOT_FOUND: return "Agent not found";
            case AGENT_ALREADY_EXISTS: return "Agent already exists";
            case AGENT_NOT_ACTIVE: return "Agent is not active";
            case AGENT_TYPE_INVALID: return "Invalid agent type";
            case SKILL_NOT_FOUND: return "Skill not found";
            case SKILL_ALREADY_INSTALLED: return "Skill already installed";
            case SKILL_INSTALL_FAILED: return "Skill installation failed";
            case SKILL_UNINSTALL_FAILED: return "Skill uninstallation failed";
            case SKILL_DEPENDENCY_ERROR: return "Skill dependency error";
            case SKILL_VERSION_MISMATCH: return "Skill version mismatch";
            case SCENE_NOT_FOUND: return "Scene not found";
            case SCENE_ALREADY_EXISTS: return "Scene already exists";
            case SCENE_MEMBER_NOT_FOUND: return "Scene member not found";
            case SCENE_GROUP_ERROR: return "Scene group error";
            case SCENE_FAILOVER_ERROR: return "Scene failover error";
            case CAPABILITY_NOT_FOUND: return "Capability not found";
            case CAPABILITY_NOT_AVAILABLE: return "Capability not available";
            case CAPABILITY_INVOKE_ERROR: return "Capability invocation error";
            case AUTH_FAILED: return "Authentication failed";
            case AUTH_TOKEN_EXPIRED: return "Authentication token expired";
            case AUTH_TOKEN_INVALID: return "Invalid authentication token";
            case PERMISSION_DENIED: return "Permission denied";
            case NETWORK_ERROR: return "Network error";
            case NETWORK_TIMEOUT: return "Network timeout";
            case NETWORK_CONNECTION_FAILED: return "Network connection failed";
            case STORAGE_ERROR: return "Storage error";
            case STORAGE_NOT_FOUND: return "Storage not found";
            case STORAGE_WRITE_ERROR: return "Storage write error";
            case STORAGE_READ_ERROR: return "Storage read error";
            case CONFIG_ERROR: return "Configuration error";
            case CONFIG_NOT_FOUND: return "Configuration not found";
            case CONFIG_INVALID: return "Invalid configuration";
            case VALIDATION_ERROR: return "Validation error";
            case INVALID_PARAMETER: return "Invalid parameter";
            case MISSING_PARAMETER: return "Missing parameter";
            case INTERNAL_ERROR: return "Internal error";
            case NOT_IMPLEMENTED: return "Feature not implemented";
            case SERVICE_UNAVAILABLE: return "Service unavailable";
            default: return "Unknown error code: " + code;
        }
    }
}
