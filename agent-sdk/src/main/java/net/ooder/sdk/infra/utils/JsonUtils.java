
package net.ooder.sdk.infra.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class JsonUtils {
    
    private JsonUtils() {}
    
    public static String toJson(Map<String, Object> map) {
        if (map == null) {
            return "null";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        
        boolean first = true;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (!first) sb.append(",");
            first = false;
            
            sb.append("\"").append(escapeString(entry.getKey())).append("\":");
            sb.append(valueToJson(entry.getValue()));
        }
        
        sb.append("}");
        return sb.toString();
    }
    
    public static String toJson(List<?> list) {
        if (list == null) {
            return "null";
        }
        
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
    
    public static String toJson(String[] array) {
        if (array == null) {
            return "null";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        
        boolean first = true;
        for (String item : array) {
            if (!first) sb.append(",");
            first = false;
            sb.append("\"").append(escapeString(item)).append("\"");
        }
        
        sb.append("]");
        return sb.toString();
    }
    
    private static String valueToJson(Object value) {
        if (value == null) {
            return "null";
        } else if (value instanceof String) {
            return "\"" + escapeString((String) value) + "\"";
        } else if (value instanceof Number) {
            return value.toString();
        } else if (value instanceof Boolean) {
            return value.toString();
        } else if (value instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> map = (Map<String, Object>) value;
            return toJson(map);
        } else if (value instanceof List) {
            return toJson((List<?>) value);
        } else if (value instanceof String[]) {
            return toJson((String[]) value);
        } else if (value instanceof Object[]) {
            StringBuilder sb = new StringBuilder("[");
            Object[] arr = (Object[]) value;
            for (int i = 0; i < arr.length; i++) {
                if (i > 0) sb.append(",");
                sb.append(valueToJson(arr[i]));
            }
            sb.append("]");
            return sb.toString();
        } else {
            return "\"" + escapeString(value.toString()) + "\"";
        }
    }
    
    private static String escapeString(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
    
    public static Map<String, Object> fromJson(String json) {
        if (json == null || json.trim().isEmpty()) {
            return new HashMap<>();
        }
        return parseObject(json.trim());
    }
    
    private static Map<String, Object> parseObject(String json) {
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
        boolean inString = false;
        
        for (int i = 0; i < json.length(); i++) {
            char c = json.charAt(i);
            
            if (c == '"' && (i == 0 || json.charAt(i-1) != '\\')) {
                inString = !inString;
            }
            
            if (!inString) {
                if (c == '{' || c == '[') depth++;
                else if (c == '}' || c == ']') depth--;
            }
            
            if (!inString && depth == 0 && c == ':' && currentKey == null) {
                currentKey = current.toString().trim();
                if (currentKey.startsWith("\"") && currentKey.endsWith("\"")) {
                    currentKey = unescapeString(currentKey.substring(1, currentKey.length() - 1));
                }
                current = new StringBuilder();
            } else if (!inString && depth == 0 && c == ',') {
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
    
    private static Object parseValue(String value) {
        if (value.equals("null")) return null;
        if (value.equals("true")) return true;
        if (value.equals("false")) return false;
        
        if (value.startsWith("\"") && value.endsWith("\"")) {
            return unescapeString(value.substring(1, value.length() - 1));
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
    
    private static List<Object> parseArray(String json) {
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
        boolean inString = false;
        
        for (int i = 0; i < json.length(); i++) {
            char c = json.charAt(i);
            
            if (c == '"' && (i == 0 || json.charAt(i-1) != '\\')) {
                inString = !inString;
            }
            
            if (!inString) {
                if (c == '{' || c == '[') depth++;
                else if (c == '}' || c == ']') depth--;
            }
            
            if (!inString && depth == 0 && c == ',') {
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
    
    private static String unescapeString(String s) {
        return s.replace("\\\"", "\"")
                .replace("\\n", "\n")
                .replace("\\r", "\r")
                .replace("\\t", "\t")
                .replace("\\\\", "\\");
    }
}
