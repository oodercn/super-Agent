
package net.ooder.sdk.service.skillcenter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.ooder.sdk.api.skill.SkillCenterClient;
import net.ooder.sdk.api.skill.SkillManifest;
import net.ooder.sdk.api.skill.SkillPackage;

public class SkillCenterClientImpl implements SkillCenterClient {
    
    private static final Logger log = LoggerFactory.getLogger(SkillCenterClientImpl.class);
    
    private static final String API_SKILLS = "/api/v1/skills";
    private static final String API_SCENES = "/api/v1/scenes";
    
    private String endpoint;
    private String authToken;
    private boolean connected = false;
    private long connectionTimeout = 10000;
    private long requestTimeout = 30000;
    
    private final Map<String, SkillPackage> skillCache = new ConcurrentHashMap<>();
    private final Map<String, SceneInfo> sceneCache = new ConcurrentHashMap<>();
    
    public SkillCenterClientImpl() {
    }
    
    public SkillCenterClientImpl(String endpoint) {
        this.endpoint = endpoint;
    }
    
    @Override
    public CompletableFuture<List<SkillPackage>> listSkills() {
        return CompletableFuture.supplyAsync(() -> {
            if (!connected || endpoint == null) {
                return new ArrayList<>(skillCache.values());
            }
            
            try {
                String response = httpGet(endpoint + API_SKILLS);
                List<SkillPackage> skills = parseSkillPackages(response);
                
                for (SkillPackage pkg : skills) {
                    skillCache.put(pkg.getSkillId(), pkg);
                }
                
                log.debug("Listed {} skills from SkillCenter", skills.size());
                return skills;
            } catch (IOException e) {
                log.warn("Failed to list skills from SkillCenter: {}, using cache", e.getMessage());
                return new ArrayList<>(skillCache.values());
            }
        });
    }
    
    @Override
    public CompletableFuture<SkillPackage> getSkill(String skillId) {
        return CompletableFuture.supplyAsync(() -> {
            if (!connected || endpoint == null) {
                return skillCache.get(skillId);
            }
            
            try {
                String response = httpGet(endpoint + API_SKILLS + "/" + skillId);
                SkillPackage pkg = parseSkillPackage(response);
                
                if (pkg != null) {
                    skillCache.put(skillId, pkg);
                }
                
                return pkg;
            } catch (IOException e) {
                log.warn("Failed to get skill {}: {}, using cache", skillId, e.getMessage());
                return skillCache.get(skillId);
            }
        });
    }
    
    @Override
    public CompletableFuture<List<SkillPackage>> searchSkills(String query) {
        return CompletableFuture.supplyAsync(() -> {
            if (!connected || endpoint == null || query == null) {
                return searchInCache(query);
            }
            
            try {
                String jsonBody = "{\"query\":\"" + escapeJson(query) + "\"}";
                String response = httpPost(endpoint + API_SKILLS + "/search", jsonBody);
                return parseSkillPackages(response);
            } catch (IOException e) {
                log.warn("Failed to search skills: {}, using cache", e.getMessage());
                return searchInCache(query);
            }
        });
    }
    
    @Override
    public CompletableFuture<List<SkillPackage>> getSkillsByScene(String sceneId) {
        return CompletableFuture.supplyAsync(() -> {
            if (!connected || endpoint == null) {
                return filterByScene(sceneId);
            }
            
            try {
                String response = httpGet(endpoint + API_SKILLS + "/scene/" + sceneId);
                return parseSkillPackages(response);
            } catch (IOException e) {
                log.warn("Failed to get skills by scene: {}, using cache", e.getMessage());
                return filterByScene(sceneId);
            }
        });
    }
    
    @Override
    public CompletableFuture<String> getDownloadUrl(String skillId, String version) {
        return CompletableFuture.supplyAsync(() -> {
            if (endpoint == null) {
                return null;
            }
            return endpoint + "/download/" + skillId + "/" + (version != null ? version : "latest");
        });
    }
    
    @Override
    public CompletableFuture<SkillManifest> getManifest(String skillId) {
        return CompletableFuture.supplyAsync(() -> {
            if (!connected || endpoint == null) {
                SkillPackage pkg = skillCache.get(skillId);
                return pkg != null ? pkg.getManifest() : null;
            }
            
            try {
                String response = httpGet(endpoint + API_SKILLS + "/" + skillId + "/manifest");
                return parseSkillManifest(response);
            } catch (IOException e) {
                log.warn("Failed to get manifest for {}: {}", skillId, e.getMessage());
                SkillPackage pkg = skillCache.get(skillId);
                return pkg != null ? pkg.getManifest() : null;
            }
        });
    }
    
    @Override
    public CompletableFuture<List<String>> getVersions(String skillId) {
        return CompletableFuture.supplyAsync(() -> {
            if (!connected || endpoint == null) {
                List<String> versions = new ArrayList<>();
                SkillPackage pkg = skillCache.get(skillId);
                if (pkg != null && pkg.getVersion() != null) {
                    versions.add(pkg.getVersion());
                }
                return versions;
            }
            
            try {
                String response = httpGet(endpoint + API_SKILLS + "/" + skillId + "/versions");
                return parseVersions(response);
            } catch (IOException e) {
                log.warn("Failed to get versions for {}: {}", skillId, e.getMessage());
                return new ArrayList<>();
            }
        });
    }
    
    @Override
    public CompletableFuture<Void> registerSkill(SkillPackage skillPackage) {
        return CompletableFuture.runAsync(() -> {
            if (skillPackage == null || skillPackage.getSkillId() == null) {
                throw new IllegalArgumentException("Skill package or skillId cannot be null");
            }
            
            skillCache.put(skillPackage.getSkillId(), skillPackage);
            
            if (connected && endpoint != null) {
                try {
                    String json = skillPackageToJson(skillPackage);
                    httpPost(endpoint + API_SKILLS, json);
                    log.info("Registered skill with SkillCenter: {}", skillPackage.getSkillId());
                } catch (IOException e) {
                    log.warn("Failed to register skill with SkillCenter: {}", e.getMessage());
                }
            } else {
                log.info("Registered skill locally: {}", skillPackage.getSkillId());
            }
        });
    }
    
    @Override
    public CompletableFuture<Void> unregisterSkill(String skillId) {
        return CompletableFuture.runAsync(() -> {
            skillCache.remove(skillId);
            
            if (connected && endpoint != null) {
                try {
                    httpDelete(endpoint + API_SKILLS + "/" + skillId);
                    log.info("Unregistered skill from SkillCenter: {}", skillId);
                } catch (IOException e) {
                    log.warn("Failed to unregister skill from SkillCenter: {}", e.getMessage());
                }
            } else {
                log.info("Unregistered skill locally: {}", skillId);
            }
        });
    }
    
    @Override
    public CompletableFuture<Void> updateSkillMetadata(String skillId, Map<String, Object> metadata) {
        return CompletableFuture.runAsync(() -> {
            SkillPackage pkg = skillCache.get(skillId);
            if (pkg != null) {
                if (pkg.getMetadata() == null) {
                    pkg.setMetadata(new ConcurrentHashMap<>());
                }
                pkg.getMetadata().putAll(metadata);
                
                if (connected && endpoint != null) {
                    try {
                        String json = mapToJson(metadata);
                        httpPut(endpoint + API_SKILLS + "/" + skillId + "/metadata", json);
                        log.info("Updated metadata for skill: {}", skillId);
                    } catch (IOException e) {
                        log.warn("Failed to update metadata with SkillCenter: {}", e.getMessage());
                    }
                }
            }
        });
    }
    
    @Override
    public CompletableFuture<List<SceneInfo>> listScenes() {
        return CompletableFuture.supplyAsync(() -> {
            if (!connected || endpoint == null) {
                return new ArrayList<>(sceneCache.values());
            }
            
            try {
                String response = httpGet(endpoint + API_SCENES);
                List<SceneInfo> scenes = parseSceneInfos(response);
                
                for (SceneInfo scene : scenes) {
                    sceneCache.put(scene.getSceneId(), scene);
                }
                
                return scenes;
            } catch (IOException e) {
                log.warn("Failed to list scenes: {}, using cache", e.getMessage());
                return new ArrayList<>(sceneCache.values());
            }
        });
    }
    
    @Override
    public CompletableFuture<SceneInfo> getScene(String sceneId) {
        return CompletableFuture.supplyAsync(() -> {
            if (!connected || endpoint == null) {
                return sceneCache.get(sceneId);
            }
            
            try {
                String response = httpGet(endpoint + API_SCENES + "/" + sceneId);
                SceneInfo scene = parseSceneInfo(response);
                
                if (scene != null) {
                    sceneCache.put(sceneId, scene);
                }
                
                return scene;
            } catch (IOException e) {
                log.warn("Failed to get scene {}: {}, using cache", sceneId, e.getMessage());
                return sceneCache.get(sceneId);
            }
        });
    }
    
    @Override
    public CompletableFuture<SceneJoinResult> joinScene(String sceneId, String agentId) {
        return CompletableFuture.supplyAsync(() -> {
            SceneJoinResult result = new SceneJoinResult();
            
            if (!connected || endpoint == null) {
                SceneInfo scene = sceneCache.get(sceneId);
                if (scene == null) {
                    result.setSuccess(false);
                    result.setError("Scene not found: " + sceneId);
                    return result;
                }
                
                if (scene.getMemberCount() >= scene.getMaxMembers()) {
                    result.setSuccess(false);
                    result.setError("Scene is full");
                    return result;
                }
                
                result.setSuccess(true);
                result.setSceneGroupId(sceneId + "-" + UUID.randomUUID().toString().substring(0, 8));
                return result;
            }
            
            try {
                String jsonBody = "{\"agentId\":\"" + escapeJson(agentId) + "\"}";
                String response = httpPost(endpoint + API_SCENES + "/" + sceneId + "/join", jsonBody);
                
                Map<String, Object> data = parseJson(response);
                result.setSuccess(getBoolean(data, "success", false));
                result.setSceneGroupId(getString(data, "sceneGroupId"));
                result.setError(getString(data, "error"));
                
                return result;
            } catch (IOException e) {
                log.warn("Failed to join scene: {}", e.getMessage());
                result.setSuccess(false);
                result.setError("Connection error: " + e.getMessage());
                return result;
            }
        });
    }
    
    @Override
    public CompletableFuture<Void> leaveScene(String sceneId, String agentId) {
        return CompletableFuture.runAsync(() -> {
            if (connected && endpoint != null) {
                try {
                    httpPost(endpoint + API_SCENES + "/" + sceneId + "/leave", "{\"agentId\":\"" + escapeJson(agentId) + "\"}");
                } catch (IOException e) {
                    log.warn("Failed to leave scene: {}", e.getMessage());
                }
            }
            log.info("Agent {} left scene {}", agentId, sceneId);
        });
    }
    
    @Override
    public String getEndpoint() {
        return endpoint;
    }
    
    @Override
    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
        log.info("SkillCenter endpoint set to: {}", endpoint);
    }
    
    @Override
    public boolean isConnected() {
        return connected && endpoint != null;
    }
    
    @Override
    public CompletableFuture<Void> connect() {
        return CompletableFuture.runAsync(() -> {
            if (endpoint == null || endpoint.isEmpty()) {
                throw new IllegalStateException("Endpoint not configured");
            }
            
            try {
                String healthUrl = endpoint + "/health";
                URL url = new URL(healthUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout((int) connectionTimeout);
                conn.setReadTimeout((int) connectionTimeout);
                
                int responseCode = conn.getResponseCode();
                conn.disconnect();
                
                if (responseCode == 200) {
                    connected = true;
                    log.info("Connected to SkillCenter: {}", endpoint);
                } else {
                    log.warn("SkillCenter returned status: {}, operating in offline mode", responseCode);
                    connected = false;
                }
            } catch (IOException e) {
                log.warn("Failed to connect to SkillCenter: {}, operating in offline mode", e.getMessage());
                connected = false;
            }
        });
    }
    
    @Override
    public CompletableFuture<Void> disconnect() {
        return CompletableFuture.runAsync(() -> {
            connected = false;
            log.info("Disconnected from SkillCenter");
        });
    }
    
    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
    
    public void addSkillToCache(SkillPackage skillPackage) {
        if (skillPackage != null && skillPackage.getSkillId() != null) {
            skillCache.put(skillPackage.getSkillId(), skillPackage);
        }
    }
    
    public void addSceneToCache(SceneInfo sceneInfo) {
        if (sceneInfo != null && sceneInfo.getSceneId() != null) {
            sceneCache.put(sceneInfo.getSceneId(), sceneInfo);
        }
    }
    
    public void clearCache() {
        skillCache.clear();
        sceneCache.clear();
    }
    
    public void setConnectionTimeout(long timeout) {
        this.connectionTimeout = timeout;
    }
    
    public void setRequestTimeout(long timeout) {
        this.requestTimeout = timeout;
    }
    
    private String httpGet(String urlStr) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        
        try {
            conn.setRequestMethod("GET");
            setAuthHeader(conn);
            conn.setRequestProperty("Accept", "application/json");
            conn.setConnectTimeout((int) requestTimeout);
            conn.setReadTimeout((int) requestTimeout);
            
            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                throw new IOException("HTTP error: " + responseCode);
            }
            
            return readResponse(conn.getInputStream());
        } finally {
            conn.disconnect();
        }
    }
    
    private String httpPost(String urlStr, String body) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        
        try {
            conn.setRequestMethod("POST");
            setAuthHeader(conn);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setConnectTimeout((int) requestTimeout);
            conn.setReadTimeout((int) requestTimeout);
            conn.setDoOutput(true);
            
            try (OutputStream os = conn.getOutputStream()) {
                os.write(body.getBytes(StandardCharsets.UTF_8));
            }
            
            int responseCode = conn.getResponseCode();
            if (responseCode != 200 && responseCode != 201) {
                throw new IOException("HTTP error: " + responseCode);
            }
            
            return readResponse(conn.getInputStream());
        } finally {
            conn.disconnect();
        }
    }
    
    private String httpPut(String urlStr, String body) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        
        try {
            conn.setRequestMethod("PUT");
            setAuthHeader(conn);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setConnectTimeout((int) requestTimeout);
            conn.setReadTimeout((int) requestTimeout);
            conn.setDoOutput(true);
            
            try (OutputStream os = conn.getOutputStream()) {
                os.write(body.getBytes(StandardCharsets.UTF_8));
            }
            
            int responseCode = conn.getResponseCode();
            if (responseCode != 200 && responseCode != 201) {
                throw new IOException("HTTP error: " + responseCode);
            }
            
            return readResponse(conn.getInputStream());
        } finally {
            conn.disconnect();
        }
    }
    
    private String httpDelete(String urlStr) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        
        try {
            conn.setRequestMethod("DELETE");
            setAuthHeader(conn);
            conn.setConnectTimeout((int) requestTimeout);
            conn.setReadTimeout((int) requestTimeout);
            
            int responseCode = conn.getResponseCode();
            if (responseCode != 200 && responseCode != 204) {
                throw new IOException("HTTP error: " + responseCode);
            }
            
            return "";
        } finally {
            conn.disconnect();
        }
    }
    
    private void setAuthHeader(HttpURLConnection conn) {
        if (authToken != null && !authToken.isEmpty()) {
            conn.setRequestProperty("Authorization", "Bearer " + authToken);
        }
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
    
    private List<SkillPackage> searchInCache(String query) {
        List<SkillPackage> results = new ArrayList<>();
        if (query == null) return results;
        
        String lowerQuery = query.toLowerCase();
        for (SkillPackage pkg : skillCache.values()) {
            if (pkg.getName() != null && pkg.getName().toLowerCase().contains(lowerQuery)) {
                results.add(pkg);
            } else if (pkg.getDescription() != null && pkg.getDescription().toLowerCase().contains(lowerQuery)) {
                results.add(pkg);
            }
        }
        return results;
    }
    
    private List<SkillPackage> filterByScene(String sceneId) {
        List<SkillPackage> results = new ArrayList<>();
        for (SkillPackage pkg : skillCache.values()) {
            if (sceneId.equals(pkg.getSceneId())) {
                results.add(pkg);
            }
        }
        return results;
    }
    
    private List<SkillPackage> parseSkillPackages(String json) {
        List<SkillPackage> packages = new ArrayList<>();
        if (json == null || json.isEmpty()) return packages;
        
        json = json.trim();
        if (!json.startsWith("[")) {
            SkillPackage pkg = parseSkillPackage(json);
            if (pkg != null) packages.add(pkg);
            return packages;
        }
        
        json = json.substring(1, json.length() - 1).trim();
        int depth = 0;
        int start = -1;
        
        for (int i = 0; i < json.length(); i++) {
            char c = json.charAt(i);
            if (c == '{') {
                if (depth == 0) start = i;
                depth++;
            } else if (c == '}') {
                depth--;
                if (depth == 0 && start >= 0) {
                    SkillPackage pkg = parseSkillPackage(json.substring(start, i + 1));
                    if (pkg != null) packages.add(pkg);
                    start = -1;
                }
            }
        }
        
        return packages;
    }
    
    private SkillPackage parseSkillPackage(String json) {
        if (json == null || json.isEmpty() || !json.trim().startsWith("{")) return null;
        
        Map<String, Object> data = parseJson(json);
        if (data.isEmpty()) return null;
        
        SkillPackage pkg = new SkillPackage();
        pkg.setSkillId(getString(data, "skillId"));
        pkg.setName(getString(data, "name"));
        pkg.setDescription(getString(data, "description"));
        pkg.setVersion(getString(data, "version"));
        pkg.setSceneId(getString(data, "sceneId"));
        pkg.setDownloadUrl(getString(data, "downloadUrl"));
        pkg.setChecksum(getString(data, "checksum"));
        pkg.setSource("skillcenter:" + endpoint);
        
        SkillManifest manifest = new SkillManifest();
        manifest.setSkillId(pkg.getSkillId());
        manifest.setName(pkg.getName());
        manifest.setDescription(pkg.getDescription());
        manifest.setVersion(pkg.getVersion());
        manifest.setSceneId(pkg.getSceneId());
        pkg.setManifest(manifest);
        
        return pkg;
    }
    
    private SkillManifest parseSkillManifest(String json) {
        if (json == null || json.isEmpty()) return null;
        
        Map<String, Object> data = parseJson(json);
        if (data.isEmpty()) return null;
        
        SkillManifest manifest = new SkillManifest();
        manifest.setSkillId(getString(data, "skillId"));
        manifest.setName(getString(data, "name"));
        manifest.setDescription(getString(data, "description"));
        manifest.setVersion(getString(data, "version"));
        manifest.setSceneId(getString(data, "sceneId"));
        manifest.setMainClass(getString(data, "mainClass"));
        manifest.setAuthor(getString(data, "author"));
        
        return manifest;
    }
    
    private List<SceneInfo> parseSceneInfos(String json) {
        List<SceneInfo> scenes = new ArrayList<>();
        if (json == null || json.isEmpty()) return scenes;
        
        json = json.trim();
        if (!json.startsWith("[")) {
            SceneInfo scene = parseSceneInfo(json);
            if (scene != null) scenes.add(scene);
            return scenes;
        }
        
        json = json.substring(1, json.length() - 1).trim();
        int depth = 0;
        int start = -1;
        
        for (int i = 0; i < json.length(); i++) {
            char c = json.charAt(i);
            if (c == '{') {
                if (depth == 0) start = i;
                depth++;
            } else if (c == '}') {
                depth--;
                if (depth == 0 && start >= 0) {
                    SceneInfo scene = parseSceneInfo(json.substring(start, i + 1));
                    if (scene != null) scenes.add(scene);
                    start = -1;
                }
            }
        }
        
        return scenes;
    }
    
    private SceneInfo parseSceneInfo(String json) {
        if (json == null || json.isEmpty() || !json.trim().startsWith("{")) return null;
        
        Map<String, Object> data = parseJson(json);
        if (data.isEmpty()) return null;
        
        SceneInfo scene = new SceneInfo();
        scene.setSceneId(getString(data, "sceneId"));
        scene.setSceneName(getString(data, "name"));
        scene.setDescription(getString(data, "description"));
        scene.setMaxMembers(getInt(data, "maxMembers", 10));
        scene.setMemberCount(getInt(data, "memberCount", 0));
        
        return scene;
    }
    
    private List<String> parseVersions(String json) {
        List<String> versions = new ArrayList<>();
        if (json == null || json.isEmpty()) return versions;
        
        json = json.trim();
        if (json.startsWith("[")) {
            json = json.substring(1, json.length() - 1);
            
            StringBuilder current = new StringBuilder();
            boolean inString = false;
            
            for (int i = 0; i < json.length(); i++) {
                char c = json.charAt(i);
                if (c == '"') {
                    inString = !inString;
                } else if (c == ',' && !inString) {
                    String v = current.toString().trim();
                    if (!v.isEmpty()) versions.add(v);
                    current = new StringBuilder();
                } else {
                    current.append(c);
                }
            }
            
            String v = current.toString().trim();
            if (!v.isEmpty()) versions.add(v);
        }
        
        return versions;
    }
    
    private Map<String, Object> parseJson(String json) {
        Map<String, Object> result = new HashMap<>();
        if (json == null || json.isEmpty()) return result;
        
        json = json.trim();
        if (!json.startsWith("{") || !json.endsWith("}")) return result;
        
        json = json.substring(1, json.length() - 1).trim();
        
        StringBuilder key = new StringBuilder();
        StringBuilder value = new StringBuilder();
        boolean inKey = true;
        boolean inString = false;
        boolean inValue = false;
        int depth = 0;
        
        for (int i = 0; i < json.length(); i++) {
            char c = json.charAt(i);
            
            if (c == '"' && (i == 0 || json.charAt(i - 1) != '\\')) {
                inString = !inString;
                if (inValue) value.append(c);
                continue;
            }
            
            if (!inString) {
                if (c == '{' || c == '[') depth++;
                if (c == '}' || c == ']') depth--;
                
                if (c == ':' && inKey && depth == 0) {
                    inKey = false;
                    inValue = true;
                    continue;
                }
                if (c == ',' && !inKey && depth == 0) {
                    String k = key.toString().trim();
                    String v = value.toString().trim();
                    if (!k.isEmpty()) result.put(k, parseValue(v));
                    key = new StringBuilder();
                    value = new StringBuilder();
                    inKey = true;
                    inValue = false;
                    continue;
                }
            }
            
            if (inKey) key.append(c);
            else if (inValue) value.append(c);
        }
        
        String k = key.toString().trim();
        String v = value.toString().trim();
        if (!k.isEmpty()) result.put(k, parseValue(v));
        
        return result;
    }
    
    private Object parseValue(String v) {
        if (v == null || v.isEmpty()) return null;
        v = v.trim();
        if ("null".equals(v)) return null;
        if ("true".equals(v)) return true;
        if ("false".equals(v)) return false;
        if (v.startsWith("\"") && v.endsWith("\"")) return v.substring(1, v.length() - 1);
        try {
            if (v.contains(".")) return Double.parseDouble(v);
            return Long.parseLong(v);
        } catch (NumberFormatException e) {
            return v;
        }
    }
    
    private String getString(Map<String, Object> data, String key) {
        Object value = data.get(key);
        return value != null ? value.toString() : null;
    }
    
    private int getInt(Map<String, Object> data, String key, int defaultValue) {
        Object value = data.get(key);
        if (value instanceof Number) return ((Number) value).intValue();
        return defaultValue;
    }
    
    private boolean getBoolean(Map<String, Object> data, String key, boolean defaultValue) {
        Object value = data.get(key);
        if (value instanceof Boolean) return (Boolean) value;
        return defaultValue;
    }
    
    private String escapeJson(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
    
    private String skillPackageToJson(SkillPackage pkg) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"skillId\":\"").append(escapeJson(pkg.getSkillId())).append("\",");
        sb.append("\"name\":\"").append(escapeJson(pkg.getName() != null ? pkg.getName() : "")).append("\",");
        sb.append("\"description\":\"").append(escapeJson(pkg.getDescription() != null ? pkg.getDescription() : "")).append("\",");
        sb.append("\"version\":\"").append(escapeJson(pkg.getVersion() != null ? pkg.getVersion() : "")).append("\",");
        sb.append("\"sceneId\":\"").append(escapeJson(pkg.getSceneId() != null ? pkg.getSceneId() : "")).append("\"");
        sb.append("}");
        return sb.toString();
    }
    
    private String mapToJson(Map<String, Object> map) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        boolean first = true;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (!first) sb.append(",");
            sb.append("\"").append(escapeJson(entry.getKey())).append("\":");
            Object value = entry.getValue();
            if (value == null) {
                sb.append("null");
            } else if (value instanceof String) {
                sb.append("\"").append(escapeJson(value.toString())).append("\"");
            } else if (value instanceof Boolean || value instanceof Number) {
                sb.append(value);
            } else {
                sb.append("\"").append(escapeJson(value.toString())).append("\"");
            }
            first = false;
        }
        sb.append("}");
        return sb.toString();
    }
}
