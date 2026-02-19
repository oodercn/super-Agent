
package net.ooder.sdk.service.storage.vfs;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VfsManager {
    
    private static final Logger log = LoggerFactory.getLogger(VfsManager.class);
    private final VfsClient client;
    private final VfsPermission permission;
    
    public VfsManager(String vfsUrl) {
        this.client = new VfsClient(vfsUrl);
        this.permission = new VfsPermission();
    }
    
    public CompletableFuture<Boolean> createDirectory(String path) { return client.createDirectory(path); }
    public CompletableFuture<Boolean> writeFile(String path, byte[] content) { return client.writeFile(path, content); }
    public CompletableFuture<byte[]> readFile(String path) { return client.readFile(path); }
    public CompletableFuture<Boolean> delete(String path) { return client.delete(path); }
    public CompletableFuture<Boolean> exists(String path) { return client.exists(path); }
    public CompletableFuture<List<String>> listFiles(String path) { return client.listFiles(path); }
    public boolean checkAccess(String path, String agentId, String op) { return permission.checkAccess(path, agentId, op); }
    public void grantAccess(String path, String agentId, String perms) { permission.grantAccess(path, agentId, perms); }
    public void revokeAccess(String path, String agentId) { permission.revokeAccess(path, agentId); }
}
