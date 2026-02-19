
package net.ooder.sdk.common.constants;

public final class SDKConstants {
    
    public static final String SDK_VERSION = "0.7.0";
    
    public static final String SDK_NAME = "Ooder Agent SDK";
    
    public static final int DEFAULT_UDP_PORT = 8888;
    
    public static final int DEFAULT_BUFFER_SIZE = 65536;
    
    public static final int DEFAULT_TIMEOUT_MS = 30000;
    
    public static final int DEFAULT_MAX_PACKET_SIZE = 65507;
    
    public static final int DEFAULT_HEARTBEAT_INTERVAL_MS = 5000;
    
    public static final int DEFAULT_HEARTBEAT_TIMEOUT_MS = 15000;
    
    public static final int DEFAULT_HEARTBEAT_LOSS_THRESHOLD = 3;
    
    public static final String SKILL_ROOT_PATH = "/skills/";
    
    public static final String SCENE_CONFIG_DIR = "config/";
    
    public static final String SCENE_VFS_DIR = "vfs/";
    
    public static final String SCENE_AUTH_DIR = "auth/";
    
    public static final String IDENTITY_FILE = "identity.yaml";
    
    public static final String SCENE_MANIFEST_FILE = "scene.yaml";
    
    public static final String SKILL_MANIFEST_FILE = "skill.yaml";
    
    public static final int FAILOVER_HEARTBEAT_INTERVAL_MS = 5000;
    
    public static final int FAILOVER_TIMEOUT_MS = 15000;
    
    public static final int FAILOVER_EVALUATION_WINDOW_MS = 30000;
    
    public static final int SHAMIR_MIN_SHARES = 3;
    
    public static final int SHAMIR_THRESHOLD = 2;
    
    private SDKConstants() {
    }
}
