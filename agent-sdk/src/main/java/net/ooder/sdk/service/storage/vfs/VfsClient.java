
package net.ooder.sdk.service.storage.vfs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VfsClient {
    
    private static final Logger log = LoggerFactory.getLogger(VfsClient.class);
    
    private static final String API_FILES = "/api/v1/files";
    private static final String API_DIRS = "/api/v1/dirs";
    
    private final String vfsUrl;
    private long timeout = 30000;
    private String authToken;
    private boolean connected = false;
    private final Map<String, byte[]> localCache = new ConcurrentHashMap<>();
    
    public VfsClient(String vfsUrl) {
        this.vfsUrl = vfsUrl;
    }
    
    public CompletableFuture<Void> connect() {
        return CompletableFuture.runAsync(() -> {
            if (vfsUrl == null || vfsUrl.isEmpty()) {
                throw new IllegalArgumentException("VFS URL not configured");
            }
            
            try {
                String healthUrl = vfsUrl + "/health";
                URL url = new URL(healthUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout((int) timeout);
                conn.setReadTimeout((int) timeout);
                
                int responseCode = conn.getResponseCode();
                conn.disconnect();
                
                if (responseCode == 200) {
                    connected = true;
                    log.info("Connected to VFS server: {}", vfsUrl);
                } else {
                    log.warn("VFS server returned status: {}", responseCode);
                    connected = false;
                }
            } catch (IOException e) {
                log.warn("Failed to connect to VFS server: {}, using local cache mode", e.getMessage());
                connected = false;
            }
        });
    }
    
    public CompletableFuture<Void> disconnect() {
        return CompletableFuture.runAsync(() -> {
            connected = false;
            localCache.clear();
            log.info("Disconnected from VFS server");
        });
    }
    
    public boolean isConnected() {
        return connected;
    }
    
    public CompletableFuture<Boolean> createDirectory(String path) {
        return CompletableFuture.supplyAsync(() -> {
            if (!connected) {
                log.debug("VFS not connected, skipping directory creation: {}", path);
                return true;
            }
            
            try {
                String urlStr = vfsUrl + API_DIRS + encodePath(path);
                URL url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                
                try {
                    conn.setRequestMethod("PUT");
                    setAuthHeader(conn);
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setConnectTimeout((int) timeout);
                    conn.setReadTimeout((int) timeout);
                    
                    int responseCode = conn.getResponseCode();
                    conn.disconnect();
                    
                    return responseCode == 200 || responseCode == 201;
                } finally {
                    conn.disconnect();
                }
            } catch (IOException e) {
                log.error("Failed to create directory {}: {}", path, e.getMessage());
                return false;
            }
        });
    }
    
    public CompletableFuture<Boolean> writeFile(String path, byte[] content) {
        return CompletableFuture.supplyAsync(() -> {
            localCache.put(path, content);
            
            if (!connected) {
                log.debug("VFS not connected, file cached locally: {}", path);
                return true;
            }
            
            try {
                String urlStr = vfsUrl + API_FILES + encodePath(path);
                URL url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                
                try {
                    conn.setRequestMethod("PUT");
                    setAuthHeader(conn);
                    conn.setRequestProperty("Content-Type", "application/octet-stream");
                    conn.setConnectTimeout((int) timeout);
                    conn.setReadTimeout((int) timeout);
                    conn.setDoOutput(true);
                    
                    try (OutputStream os = conn.getOutputStream()) {
                        os.write(content);
                    }
                    
                    int responseCode = conn.getResponseCode();
                    conn.disconnect();
                    
                    return responseCode == 200 || responseCode == 201;
                } finally {
                    conn.disconnect();
                }
            } catch (IOException e) {
                log.error("Failed to write file {}: {}", path, e.getMessage());
                return false;
            }
        });
    }
    
    public CompletableFuture<byte[]> readFile(String path) {
        return CompletableFuture.supplyAsync(() -> {
            if (connected) {
                try {
                    String urlStr = vfsUrl + API_FILES + encodePath(path);
                    URL url = new URL(urlStr);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    
                    try {
                        conn.setRequestMethod("GET");
                        setAuthHeader(conn);
                        conn.setConnectTimeout((int) timeout);
                        conn.setReadTimeout((int) timeout);
                        
                        int responseCode = conn.getResponseCode();
                        if (responseCode == 200) {
                            byte[] content = readAllBytes(conn.getInputStream());
                            localCache.put(path, content);
                            conn.disconnect();
                            return content;
                        }
                        conn.disconnect();
                    } finally {
                        conn.disconnect();
                    }
                } catch (IOException e) {
                    log.warn("Failed to read file {} from VFS: {}, using cache", path, e.getMessage());
                }
            }
            
            return localCache.get(path);
        });
    }
    
    public CompletableFuture<Boolean> delete(String path) {
        return CompletableFuture.supplyAsync(() -> {
            localCache.remove(path);
            
            if (!connected) {
                return true;
            }
            
            try {
                String urlStr = vfsUrl + API_FILES + encodePath(path);
                URL url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                
                try {
                    conn.setRequestMethod("DELETE");
                    setAuthHeader(conn);
                    conn.setConnectTimeout((int) timeout);
                    conn.setReadTimeout((int) timeout);
                    
                    int responseCode = conn.getResponseCode();
                    conn.disconnect();
                    
                    return responseCode == 200 || responseCode == 204;
                } finally {
                    conn.disconnect();
                }
            } catch (IOException e) {
                log.error("Failed to delete {}: {}", path, e.getMessage());
                return false;
            }
        });
    }
    
    public CompletableFuture<Boolean> exists(String path) {
        return CompletableFuture.supplyAsync(() -> {
            if (localCache.containsKey(path)) {
                return true;
            }
            
            if (!connected) {
                return false;
            }
            
            try {
                String urlStr = vfsUrl + API_FILES + encodePath(path) + "/exists";
                URL url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                
                try {
                    conn.setRequestMethod("GET");
                    setAuthHeader(conn);
                    conn.setConnectTimeout((int) timeout);
                    conn.setReadTimeout((int) timeout);
                    
                    int responseCode = conn.getResponseCode();
                    conn.disconnect();
                    
                    return responseCode == 200;
                } finally {
                    conn.disconnect();
                }
            } catch (IOException e) {
                return false;
            }
        });
    }
    
    public CompletableFuture<List<String>> listFiles(String path) {
        return CompletableFuture.supplyAsync(() -> {
            if (!connected) {
                return new ArrayList<>(localCache.keySet());
            }
            
            try {
                String urlStr = vfsUrl + API_FILES + encodePath(path) + "/list";
                URL url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                
                try {
                    conn.setRequestMethod("GET");
                    setAuthHeader(conn);
                    conn.setRequestProperty("Accept", "application/json");
                    conn.setConnectTimeout((int) timeout);
                    conn.setReadTimeout((int) timeout);
                    
                    int responseCode = conn.getResponseCode();
                    if (responseCode == 200) {
                        String response = readResponse(conn.getInputStream());
                        conn.disconnect();
                        return parseFileList(response);
                    }
                    conn.disconnect();
                } finally {
                    conn.disconnect();
                }
            } catch (IOException e) {
                log.warn("Failed to list files: {}", e.getMessage());
            }
            
            return new ArrayList<>(localCache.keySet());
        });
    }
    
    public CompletableFuture<Boolean> copy(String source, String target) {
        return CompletableFuture.supplyAsync(() -> {
            byte[] content = localCache.get(source);
            if (content != null) {
                localCache.put(target, content);
            }
            
            if (!connected) {
                return content != null;
            }
            
            try {
                String urlStr = vfsUrl + API_FILES + encodePath(source) + "/copy?target=" + encodePath(target);
                URL url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                
                try {
                    conn.setRequestMethod("POST");
                    setAuthHeader(conn);
                    conn.setConnectTimeout((int) timeout);
                    conn.setReadTimeout((int) timeout);
                    
                    int responseCode = conn.getResponseCode();
                    conn.disconnect();
                    
                    return responseCode == 200;
                } finally {
                    conn.disconnect();
                }
            } catch (IOException e) {
                log.error("Failed to copy {} to {}: {}", source, target, e.getMessage());
                return false;
            }
        });
    }
    
    public CompletableFuture<Boolean> move(String source, String target) {
        return CompletableFuture.supplyAsync(() -> {
            byte[] content = localCache.remove(source);
            if (content != null) {
                localCache.put(target, content);
            }
            
            if (!connected) {
                return content != null;
            }
            
            try {
                String urlStr = vfsUrl + API_FILES + encodePath(source) + "/move?target=" + encodePath(target);
                URL url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                
                try {
                    conn.setRequestMethod("POST");
                    setAuthHeader(conn);
                    conn.setConnectTimeout((int) timeout);
                    conn.setReadTimeout((int) timeout);
                    
                    int responseCode = conn.getResponseCode();
                    conn.disconnect();
                    
                    return responseCode == 200;
                } finally {
                    conn.disconnect();
                }
            } catch (IOException e) {
                log.error("Failed to move {} to {}: {}", source, target, e.getMessage());
                return false;
            }
        });
    }
    
    public void setTimeout(long timeoutMs) {
        this.timeout = timeoutMs;
    }
    
    public long getTimeout() {
        return timeout;
    }
    
    public String getVfsUrl() {
        return vfsUrl;
    }
    
    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
    
    public String getAuthToken() {
        return authToken;
    }
    
    private void setAuthHeader(HttpURLConnection conn) {
        if (authToken != null && !authToken.isEmpty()) {
            conn.setRequestProperty("Authorization", "Bearer " + authToken);
        }
    }
    
    private String encodePath(String path) {
        try {
            return java.net.URLEncoder.encode(path, "UTF-8").replace("%2F", "/");
        } catch (java.io.UnsupportedEncodingException e) {
            return path;
        }
    }
    
    private byte[] readAllBytes(InputStream is) throws IOException {
        java.io.ByteArrayOutputStream buffer = new java.io.ByteArrayOutputStream();
        byte[] data = new byte[4096];
        int nRead;
        while ((nRead = is.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        return buffer.toByteArray();
    }
    
    private String readResponse(InputStream is) throws IOException {
        StringBuilder response = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        }
        return response.toString();
    }
    
    private List<String> parseFileList(String json) {
        List<String> files = new ArrayList<>();
        if (json == null || json.isEmpty()) {
            return files;
        }
        
        json = json.trim();
        if (json.startsWith("[") && json.endsWith("]")) {
            json = json.substring(1, json.length() - 1);
            
            StringBuilder current = new StringBuilder();
            boolean inString = false;
            
            for (int i = 0; i < json.length(); i++) {
                char c = json.charAt(i);
                if (c == '"' && (i == 0 || json.charAt(i - 1) != '\\')) {
                    inString = !inString;
                } else if (c == ',' && !inString) {
                    String file = current.toString().trim();
                    if (file.startsWith("\"") && file.endsWith("\"")) {
                        file = file.substring(1, file.length() - 1);
                    }
                    if (!file.isEmpty()) {
                        files.add(file);
                    }
                    current = new StringBuilder();
                } else {
                    current.append(c);
                }
            }
            
            String file = current.toString().trim();
            if (file.startsWith("\"") && file.endsWith("\"")) {
                file = file.substring(1, file.length() - 1);
            }
            if (!file.isEmpty()) {
                files.add(file);
            }
        }
        
        return files;
    }
}
