
package net.ooder.sdk.core.skill.discovery;

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
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.ooder.sdk.api.skill.Capability;
import net.ooder.sdk.api.skill.SkillDiscoverer;
import net.ooder.sdk.api.skill.SkillManifest;
import net.ooder.sdk.api.skill.SkillPackage;
import net.ooder.sdk.common.enums.DiscoveryMethod;

public class SkillCenterDiscoverer implements SkillDiscoverer {
    
    private static final Logger log = LoggerFactory.getLogger(SkillCenterDiscoverer.class);
    
    private static final String API_SKILLS = "/api/v1/skills";
    private static final String API_SEARCH = "/api/v1/skills/search";
    private static final String API_BY_SCENE = "/api/v1/skills/scene";
    private static final String API_BY_CAPABILITY = "/api/v1/skills/capability";
    
    private String endpoint;
    private long timeout = 10000;
    private DiscoveryFilter filter;
    private String authToken;
    
    public SkillCenterDiscoverer() {
    }
    
    public SkillCenterDiscoverer(String endpoint) {
        this.endpoint = endpoint;
    }
    
    @Override
    public CompletableFuture<List<SkillPackage>> discover() {
        return CompletableFuture.supplyAsync(() -> {
            if (!isAvailable()) {
                log.warn("SkillCenter endpoint not configured");
                return new ArrayList<SkillPackage>();
            }
            
            try {
                String response = httpGet(endpoint + API_SKILLS);
                List<SkillPackage> packages = parseSkillPackages(response);
                
                List<SkillPackage> filtered = new ArrayList<>();
                for (SkillPackage pkg : packages) {
                    if (passesFilter(pkg)) {
                        filtered.add(pkg);
                    }
                }
                
                log.info("Discovered {} skill packages from SkillCenter", filtered.size());
                return filtered;
            } catch (Exception e) {
                log.error("Failed to discover skills from SkillCenter: {}", e.getMessage());
                return new ArrayList<SkillPackage>();
            }
        });
    }
    
    @Override
    public CompletableFuture<SkillPackage> discover(String skillId) {
        return CompletableFuture.supplyAsync(() -> {
            if (!isAvailable()) {
                log.warn("SkillCenter endpoint not configured");
                return null;
            }
            
            try {
                String response = httpGet(endpoint + API_SKILLS + "/" + skillId);
                SkillPackage pkg = parseSkillPackage(response);
                
                if (pkg != null && passesFilter(pkg)) {
                    log.debug("Discovered skill {} from SkillCenter", skillId);
                    return pkg;
                }
                
                return null;
            } catch (Exception e) {
                log.error("Failed to discover skill {} from SkillCenter: {}", skillId, e.getMessage());
                return null;
            }
        });
    }
    
    @Override
    public CompletableFuture<List<SkillPackage>> discoverByScene(String sceneId) {
        return CompletableFuture.supplyAsync(() -> {
            if (!isAvailable()) {
                return new ArrayList<SkillPackage>();
            }
            
            try {
                String response = httpGet(endpoint + API_BY_SCENE + "/" + sceneId);
                List<SkillPackage> packages = parseSkillPackages(response);
                
                List<SkillPackage> filtered = new ArrayList<>();
                for (SkillPackage pkg : packages) {
                    if (passesFilter(pkg)) {
                        filtered.add(pkg);
                    }
                }
                
                log.debug("Found {} skills for scene {} from SkillCenter", filtered.size(), sceneId);
                return filtered;
            } catch (Exception e) {
                log.error("Failed to discover skills by scene: {}", e.getMessage());
                return new ArrayList<SkillPackage>();
            }
        });
    }
    
    @Override
    public CompletableFuture<List<SkillPackage>> search(String query) {
        return CompletableFuture.supplyAsync(() -> {
            if (!isAvailable() || query == null || query.isEmpty()) {
                return new ArrayList<SkillPackage>();
            }
            
            try {
                String jsonBody = "{\"query\":\"" + escapeJson(query) + "\"}";
                String response = httpPost(endpoint + API_SEARCH, jsonBody);
                List<SkillPackage> packages = parseSkillPackages(response);
                
                List<SkillPackage> filtered = new ArrayList<>();
                for (SkillPackage pkg : packages) {
                    if (passesFilter(pkg)) {
                        filtered.add(pkg);
                    }
                }
                
                log.debug("Found {} skills matching query from SkillCenter", filtered.size());
                return filtered;
            } catch (Exception e) {
                log.error("Failed to search skills: {}", e.getMessage());
                return new ArrayList<SkillPackage>();
            }
        });
    }
    
    @Override
    public CompletableFuture<List<SkillPackage>> searchByCapability(String capabilityId) {
        return CompletableFuture.supplyAsync(() -> {
            if (!isAvailable() || capabilityId == null || capabilityId.isEmpty()) {
                return new ArrayList<SkillPackage>();
            }
            
            try {
                String response = httpGet(endpoint + API_BY_CAPABILITY + "/" + capabilityId);
                List<SkillPackage> packages = parseSkillPackages(response);
                
                List<SkillPackage> filtered = new ArrayList<>();
                for (SkillPackage pkg : packages) {
                    if (passesFilter(pkg)) {
                        filtered.add(pkg);
                    }
                }
                
                log.debug("Found {} skills with capability {} from SkillCenter", filtered.size(), capabilityId);
                return filtered;
            } catch (Exception e) {
                log.error("Failed to search skills by capability: {}", e.getMessage());
                return new ArrayList<SkillPackage>();
            }
        });
    }
    
    @Override
    public DiscoveryMethod getMethod() {
        return DiscoveryMethod.SKILL_CENTER;
    }
    
    @Override
    public boolean isAvailable() {
        return endpoint != null && !endpoint.isEmpty();
    }
    
    @Override
    public void setTimeout(long timeoutMs) {
        this.timeout = timeoutMs;
    }
    
    @Override
    public long getTimeout() {
        return timeout;
    }
    
    @Override
    public void setFilter(DiscoveryFilter filter) {
        this.filter = filter;
    }
    
    @Override
    public DiscoveryFilter getFilter() {
        return filter;
    }
    
    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }
    
    public String getEndpoint() {
        return endpoint;
    }
    
    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
    
    public String getAuthToken() {
        return authToken;
    }
    
    private String httpGet(String urlStr) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        
        try {
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-Type", "application/json");
            
            if (authToken != null && !authToken.isEmpty()) {
                conn.setRequestProperty("Authorization", "Bearer " + authToken);
            }
            
            conn.setConnectTimeout((int) timeout);
            conn.setReadTimeout((int) timeout);
            
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
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-Type", "application/json");
            
            if (authToken != null && !authToken.isEmpty()) {
                conn.setRequestProperty("Authorization", "Bearer " + authToken);
            }
            
            conn.setConnectTimeout((int) timeout);
            conn.setReadTimeout((int) timeout);
            conn.setDoOutput(true);
            
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = body.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
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
    
    private List<SkillPackage> parseSkillPackages(String json) {
        List<SkillPackage> packages = new ArrayList<>();
        if (json == null || json.isEmpty()) {
            return packages;
        }
        
        json = json.trim();
        if (!json.startsWith("[")) {
            SkillPackage pkg = parseSkillPackage(json);
            if (pkg != null) {
                packages.add(pkg);
            }
            return packages;
        }
        
        json = json.substring(1, json.length() - 1).trim();
        
        int depth = 0;
        int start = -1;
        
        for (int i = 0; i < json.length(); i++) {
            char c = json.charAt(i);
            if (c == '{') {
                if (depth == 0) {
                    start = i;
                }
                depth++;
            } else if (c == '}') {
                depth--;
                if (depth == 0 && start >= 0) {
                    String objJson = json.substring(start, i + 1);
                    SkillPackage pkg = parseSkillPackage(objJson);
                    if (pkg != null) {
                        packages.add(pkg);
                    }
                    start = -1;
                }
            }
        }
        
        return packages;
    }
    
    private SkillPackage parseSkillPackage(String json) {
        if (json == null || json.isEmpty() || !json.trim().startsWith("{")) {
            return null;
        }
        
        Map<String, Object> data = parseJson(json);
        if (data.isEmpty()) {
            return null;
        }
        
        SkillPackage pkg = new SkillPackage();
        SkillManifest manifest = new SkillManifest();
        
        pkg.setSkillId(getString(data, "skillId"));
        pkg.setName(getString(data, "name"));
        pkg.setDescription(getString(data, "description"));
        pkg.setVersion(getString(data, "version"));
        pkg.setSceneId(getString(data, "sceneId"));
        pkg.setDownloadUrl(getString(data, "downloadUrl"));
        pkg.setChecksum(getString(data, "checksum"));
        pkg.setSource("skillcenter:" + endpoint);
        
        Object sizeObj = data.get("size");
        if (sizeObj instanceof Number) {
            pkg.setSize(((Number) sizeObj).longValue());
        }
        
        manifest.setSkillId(pkg.getSkillId());
        manifest.setName(pkg.getName());
        manifest.setDescription(pkg.getDescription());
        manifest.setVersion(pkg.getVersion());
        manifest.setSceneId(pkg.getSceneId());
        manifest.setMainClass(getString(data, "mainClass"));
        manifest.setAuthor(getString(data, "author"));
        manifest.setLicense(getString(data, "license"));
        
        pkg.setManifest(manifest);
        
        return pkg;
    }
    
    private Map<String, Object> parseJson(String json) {
        Map<String, Object> result = new HashMap<>();
        if (json == null || json.isEmpty()) {
            return result;
        }
        
        json = json.trim();
        if (!json.startsWith("{") || !json.endsWith("}")) {
            return result;
        }
        
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
                    if (!k.isEmpty()) {
                        result.put(k, parseValue(v));
                    }
                    key = new StringBuilder();
                    value = new StringBuilder();
                    inKey = true;
                    inValue = false;
                    continue;
                }
            }
            
            if (inKey) {
                key.append(c);
            } else if (inValue) {
                value.append(c);
            }
        }
        
        String k = key.toString().trim();
        String v = value.toString().trim();
        if (!k.isEmpty()) {
            result.put(k, parseValue(v));
        }
        
        return result;
    }
    
    private Object parseValue(String v) {
        if (v == null || v.isEmpty()) return null;
        v = v.trim();
        if ("null".equals(v)) return null;
        if ("true".equals(v)) return true;
        if ("false".equals(v)) return false;
        if (v.startsWith("\"") && v.endsWith("\"")) {
            return v.substring(1, v.length() - 1);
        }
        try {
            if (v.contains(".")) {
                return Double.parseDouble(v);
            }
            return Long.parseLong(v);
        } catch (NumberFormatException e) {
            return v;
        }
    }
    
    private String getString(Map<String, Object> data, String key) {
        Object value = data.get(key);
        return value != null ? value.toString() : null;
    }
    
    private String escapeJson(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
    
    private boolean passesFilter(SkillPackage pkg) {
        if (filter == null) {
            return true;
        }
        if (filter.getSceneId() != null && !filter.getSceneId().equals(pkg.getSceneId())) {
            return false;
        }
        if (filter.getVersion() != null && !filter.getVersion().equals(pkg.getVersion())) {
            return false;
        }
        return true;
    }
}
