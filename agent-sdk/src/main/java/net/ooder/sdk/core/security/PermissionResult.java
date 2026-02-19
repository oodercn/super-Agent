package net.ooder.sdk.core.security;

public class PermissionResult {
    
    private boolean granted;
    private String permissionId;
    private String resource;
    private String action;
    private String reason;
    
    public static PermissionResult granted(String permissionId, String resource, String action) {
        PermissionResult result = new PermissionResult();
        result.setGranted(true);
        result.setPermissionId(permissionId);
        result.setResource(resource);
        result.setAction(action);
        return result;
    }
    
    public static PermissionResult denied(String permissionId, String resource, String action, String reason) {
        PermissionResult result = new PermissionResult();
        result.setGranted(false);
        result.setPermissionId(permissionId);
        result.setResource(resource);
        result.setAction(action);
        result.setReason(reason);
        return result;
    }
    
    public boolean isGranted() { return granted; }
    public void setGranted(boolean granted) { this.granted = granted; }
    
    public String getPermissionId() { return permissionId; }
    public void setPermissionId(String permissionId) { this.permissionId = permissionId; }
    
    public String getResource() { return resource; }
    public void setResource(String resource) { this.resource = resource; }
    
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
