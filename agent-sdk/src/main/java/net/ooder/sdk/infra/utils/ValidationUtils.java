
package net.ooder.sdk.infra.utils;

import java.util.Collection;
import java.util.Map;
import java.util.regex.Pattern;

public final class ValidationUtils {
    
    private static final Pattern ID_PATTERN = Pattern.compile("^[a-zA-Z][a-zA-Z0-9_-]{0,63}$");
    private static final Pattern CAP_ID_PATTERN = Pattern.compile("^[a-z][a-z0-9-]*-[a-z][a-z0-9-]*$");
    private static final Pattern SCENE_PREFIX_PATTERN = Pattern.compile("^[a-z][a-z0-9]{0,31}$");
    private static final Pattern VERSION_PATTERN = Pattern.compile("^\\d+\\.\\d+\\.\\d+(-[a-zA-Z0-9]+)?$");
    private static final Pattern URL_PATTERN = Pattern.compile("^[a-zA-Z][a-zA-Z0-9+.-]*://[^\\s]+$");
    
    private ValidationUtils() {
    }
    
    public static void notNull(Object obj, String name) {
        if (obj == null) {
            throw new IllegalArgumentException(name + " cannot be null");
        }
    }
    
    public static void notEmpty(String str, String name) {
        if (str == null || str.trim().isEmpty()) {
            throw new IllegalArgumentException(name + " cannot be null or empty");
        }
    }
    
    public static void notEmpty(Collection<?> collection, String name) {
        if (collection == null || collection.isEmpty()) {
            throw new IllegalArgumentException(name + " cannot be null or empty");
        }
    }
    
    public static void notEmpty(Map<?, ?> map, String name) {
        if (map == null || map.isEmpty()) {
            throw new IllegalArgumentException(name + " cannot be null or empty");
        }
    }
    
    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new IllegalArgumentException(message);
        }
    }
    
    public static void isValidId(String id, String name) {
        notEmpty(id, name);
        if (!ID_PATTERN.matcher(id).matches()) {
            throw new IllegalArgumentException(name + " format is invalid: " + id + 
                ". Expected: starts with letter, alphanumeric with underscore or hyphen, max 64 chars");
        }
    }
    
    public static void isValidCapId(String capId, String name) {
        notEmpty(capId, name);
        if (!CAP_ID_PATTERN.matcher(capId).matches()) {
            throw new IllegalArgumentException(name + " format is invalid: " + capId + 
                ". Expected: {scene-prefix}-{function-name} format");
        }
    }
    
    public static void isValidScenePrefix(String prefix, String name) {
        notEmpty(prefix, name);
        if (!SCENE_PREFIX_PATTERN.matcher(prefix).matches()) {
            throw new IllegalArgumentException(name + " format is invalid: " + prefix + 
                ". Expected: lowercase alphanumeric, starts with letter, max 32 chars");
        }
    }
    
    public static void isValidVersion(String version, String name) {
        notEmpty(version, name);
        if (!VERSION_PATTERN.matcher(version).matches()) {
            throw new IllegalArgumentException(name + " format is invalid: " + version + 
                ". Expected: semantic version (e.g., 1.0.0)");
        }
    }
    
    public static void isValidUrl(String url, String name) {
        notEmpty(url, name);
        if (!URL_PATTERN.matcher(url).matches()) {
            throw new IllegalArgumentException(name + " format is invalid: " + url);
        }
    }
    
    public static void inRange(int value, int min, int max, String name) {
        if (value < min || value > max) {
            throw new IllegalArgumentException(name + " must be between " + min + " and " + max + 
                ", but was " + value);
        }
    }
    
    public static void inRange(long value, long min, long max, String name) {
        if (value < min || value > max) {
            throw new IllegalArgumentException(name + " must be between " + min + " and " + max + 
                ", but was " + value);
        }
    }
    
    public static void positive(int value, String name) {
        if (value <= 0) {
            throw new IllegalArgumentException(name + " must be positive, but was " + value);
        }
    }
    
    public static void positive(long value, String name) {
        if (value <= 0) {
            throw new IllegalArgumentException(name + " must be positive, but was " + value);
        }
    }
    
    public static void nonNegative(int value, String name) {
        if (value < 0) {
            throw new IllegalArgumentException(name + " must be non-negative, but was " + value);
        }
    }
    
    public static void nonNegative(long value, String name) {
        if (value < 0) {
            throw new IllegalArgumentException(name + " must be non-negative, but was " + value);
        }
    }
    
    public static <T> T requireNonNull(T obj, String name) {
        if (obj == null) {
            throw new IllegalArgumentException(name + " cannot be null");
        }
        return obj;
    }
    
    public static String requireNonEmpty(String str, String name) {
        if (str == null || str.trim().isEmpty()) {
            throw new IllegalArgumentException(name + " cannot be null or empty");
        }
        return str;
    }
}
