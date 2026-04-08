package net.ooder.mvp.skill.scene.config.sdk;

import java.util.LinkedHashMap;
import java.util.Map;

public class ConfigNode {

    private final Map<String, Object> data;

    public ConfigNode() {
        this.data = new LinkedHashMap<>();
    }

    public ConfigNode(Map<String, Object> data) {
        this.data = data != null ? new LinkedHashMap<>(data) : new LinkedHashMap<>();
    }

    public Map<String, Object> getData() {
        return data;
    }

    public Object get(String key) {
        return data.get(key);
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getNested(String path) {
        String[] parts = path.split("\\.");
        Map<String, Object> current = data;
        
        for (int i = 0; i < parts.length - 1; i++) {
            Object value = current.get(parts[i]);
            if (value instanceof Map) {
                current = (Map<String, Object>) value;
            } else {
                return null;
            }
        }
        
        Object result = current.get(parts[parts.length - 1]);
        if (result instanceof Map) {
            return (Map<String, Object>) result;
        }
        return null;
    }
    
    public Object getNestedValue(String path) {
        String[] parts = path.split("\\.");
        Map<String, Object> current = data;
        
        for (int i = 0; i < parts.length - 1; i++) {
            Object value = current.get(parts[i]);
            if (value instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> next = (Map<String, Object>) value;
                current = next;
            } else {
                return null;
            }
        }
        
        return current.get(parts[parts.length - 1]);
    }
    
    public String getNestedString(String path) {
        Object value = getNestedValue(path);
        return value != null ? value.toString() : null;
    }
    
    public String getNestedString(String path, String defaultValue) {
        String value = getNestedString(path);
        return value != null ? value : defaultValue;
    }

    public void put(String key, Object value) {
        data.put(key, value);
    }

    public void putNested(String path, Object value) {
        String[] parts = path.split("\\.");
        Map<String, Object> current = data;
        
        for (int i = 0; i < parts.length - 1; i++) {
            @SuppressWarnings("unchecked")
            Map<String, Object> child = (Map<String, Object>) current.get(parts[i]);
            if (child == null) {
                child = new LinkedHashMap<>();
                current.put(parts[i], child);
            }
            current = child;
        }
        
        current.put(parts[parts.length - 1], value);
    }

    public void putAll(Map<String, Object> map) {
        if (map != null) {
            data.putAll(map);
        }
    }

    public boolean containsKey(String key) {
        return data.containsKey(key);
    }

    public void remove(String key) {
        data.remove(key);
    }

    public String getString(String key) {
        Object value = get(key);
        return value != null ? value.toString() : null;
    }

    public String getString(String key, String defaultValue) {
        String value = getString(key);
        return value != null ? value : defaultValue;
    }

    public Integer getInteger(String key) {
        Object value = get(key);
        if (value instanceof Integer) {
            return (Integer) value;
        }
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        if (value instanceof String) {
            try {
                return Integer.parseInt((String) value);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    public Integer getInteger(String key, Integer defaultValue) {
        Integer value = getInteger(key);
        return value != null ? value : defaultValue;
    }

    public Boolean getBoolean(String key) {
        Object value = get(key);
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        if (value instanceof String) {
            return Boolean.parseBoolean((String) value);
        }
        return null;
    }

    public Boolean getBoolean(String key, Boolean defaultValue) {
        Boolean value = getBoolean(key);
        return value != null ? value : defaultValue;
    }

    @Override
    public String toString() {
        return data.toString();
    }
}
