
package net.ooder.sdk.service.storage.persistence;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonStorage {
    
    private static final Logger log = LoggerFactory.getLogger(JsonStorage.class);
    
    private final String basePath;
    private final String fileExtension;
    
    public JsonStorage(String basePath) {
        this.basePath = basePath;
        this.fileExtension = ".json";
        
        File dir = new File(basePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }
    
    public void save(String key, Map<String, Object> data) throws IOException {
        String filePath = getFilePath(key);
        String json = toJson(data);
        
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(filePath), StandardCharsets.UTF_8))) {
            writer.write(json);
        }
        
        log.debug("Saved data to: {}", filePath);
    }
    
    public Map<String, Object> load(String key) throws IOException {
        String filePath = getFilePath(key);
        File file = new File(filePath);
        
        if (!file.exists()) {
            return null;
        }
        
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
        }
        
        return fromJson(content.toString());
    }
    
    public void delete(String key) {
        String filePath = getFilePath(key);
        File file = new File(filePath);
        
        if (file.exists()) {
            file.delete();
            log.debug("Deleted: {}", filePath);
        }
    }
    
    public boolean exists(String key) {
        return new File(getFilePath(key)).exists();
    }
    
    public List<String> listKeys() {
        List<String> keys = new ArrayList<>();
        File dir = new File(basePath);
        
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles((d, name) -> name.endsWith(fileExtension));
            if (files != null) {
                for (File file : files) {
                    String name = file.getName();
                    keys.add(name.substring(0, name.length() - fileExtension.length()));
                }
            }
        }
        
        return keys;
    }
    
    public void saveList(String key, List<Map<String, Object>> list) throws IOException {
        Map<String, Object> wrapper = new HashMap<>();
        wrapper.put("items", list);
        wrapper.put("count", list.size());
        save(key, wrapper);
    }
    
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> loadList(String key) throws IOException {
        Map<String, Object> data = load(key);
        if (data == null) {
            return new ArrayList<>();
        }
        
        Object items = data.get("items");
        if (items instanceof List) {
            return (List<Map<String, Object>>) items;
        }
        
        return new ArrayList<>();
    }
    
    private String getFilePath(String key) {
        return basePath + File.separator + sanitizeKey(key) + fileExtension;
    }
    
    private String sanitizeKey(String key) {
        return key.replaceAll("[^a-zA-Z0-9_-]", "_");
    }
    
    private String toJson(Map<String, Object> data) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        
        boolean first = true;
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            if (!first) sb.append(",");
            first = false;
            
            sb.append("\"").append(entry.getKey()).append("\":");
            sb.append(valueToJson(entry.getValue()));
        }
        
        sb.append("}");
        return sb.toString();
    }
    
    private String valueToJson(Object value) {
        if (value == null) {
            return "null";
        } else if (value instanceof String) {
            return "\"" + escapeString((String) value) + "\"";
        } else if (value instanceof Number || value instanceof Boolean) {
            return value.toString();
        } else if (value instanceof Map) {
            return toJson((Map<String, Object>) value);
        } else if (value instanceof List) {
            return listToJson((List<?>) value);
        } else {
            return "\"" + escapeString(value.toString()) + "\"";
        }
    }
    
    private String listToJson(List<?> list) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        
        boolean first = true;
        for (Object item : list) {
            if (!first) sb.append(",");
            first = false;
            sb.append(valueToJson(item));
        }
        
        sb.append("]");
        return sb.toString();
    }
    
    private String escapeString(String s) {
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
    
    private Map<String, Object> fromJson(String json) {
        return parseObject(json.trim());
    }
    
    private Map<String, Object> parseObject(String json) {
        Map<String, Object> result = new HashMap<>();
        
        if (!json.startsWith("{") || !json.endsWith("}")) {
            return result;
        }
        
        json = json.substring(1, json.length() - 1).trim();
        if (json.isEmpty()) {
            return result;
        }
        
        int depth = 0;
        StringBuilder current = new StringBuilder();
        String currentKey = null;
        
        for (int i = 0; i < json.length(); i++) {
            char c = json.charAt(i);
            
            if (c == '{' || c == '[') depth++;
            else if (c == '}' || c == ']') depth--;
            
            if (depth == 0 && c == ':' && currentKey == null) {
                currentKey = current.toString().trim();
                if (currentKey.startsWith("\"") && currentKey.endsWith("\"")) {
                    currentKey = currentKey.substring(1, currentKey.length() - 1);
                }
                current = new StringBuilder();
            } else if (depth == 0 && c == ',') {
                if (currentKey != null) {
                    result.put(currentKey, parseValue(current.toString().trim()));
                }
                currentKey = null;
                current = new StringBuilder();
            } else {
                current.append(c);
            }
        }
        
        if (currentKey != null) {
            result.put(currentKey, parseValue(current.toString().trim()));
        }
        
        return result;
    }
    
    private Object parseValue(String value) {
        if (value.equals("null")) return null;
        if (value.equals("true")) return true;
        if (value.equals("false")) return false;
        
        if (value.startsWith("\"") && value.endsWith("\"")) {
            return value.substring(1, value.length() - 1)
                       .replace("\\\"", "\"")
                       .replace("\\n", "\n")
                       .replace("\\r", "\r")
                       .replace("\\t", "\t")
                       .replace("\\\\", "\\");
        }
        
        if (value.startsWith("{")) return parseObject(value);
        if (value.startsWith("[")) return parseArray(value);
        
        try {
            if (value.contains(".")) {
                return Double.parseDouble(value);
            } else {
                return Long.parseLong(value);
            }
        } catch (NumberFormatException e) {
            return value;
        }
    }
    
    private List<Object> parseArray(String json) {
        List<Object> result = new ArrayList<>();
        
        if (!json.startsWith("[") || !json.endsWith("]")) {
            return result;
        }
        
        json = json.substring(1, json.length() - 1).trim();
        if (json.isEmpty()) {
            return result;
        }
        
        int depth = 0;
        StringBuilder current = new StringBuilder();
        
        for (int i = 0; i < json.length(); i++) {
            char c = json.charAt(i);
            
            if (c == '{' || c == '[') depth++;
            else if (c == '}' || c == ']') depth--;
            
            if (depth == 0 && c == ',') {
                result.add(parseValue(current.toString().trim()));
                current = new StringBuilder();
            } else {
                current.append(c);
            }
        }
        
        if (current.length() > 0) {
            result.add(parseValue(current.toString().trim()));
        }
        
        return result;
    }
}
