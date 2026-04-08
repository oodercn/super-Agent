package net.ooder.mvp.skill.scene.config.service;

import net.ooder.mvp.skill.scene.config.sdk.ConfigNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ConfigInheritanceResolver {

    private static final Logger log = LoggerFactory.getLogger(ConfigInheritanceResolver.class);

    private static final Pattern INHERIT_PATTERN = Pattern.compile("\\$\\{inherit(?::([^}]*))?\\}");
    private static final Pattern MERGE_PATTERN = Pattern.compile("^\\$\\{merge\\}$");
    private static final Pattern APPEND_PATTERN = Pattern.compile("^\\$\\{append\\}$");

    public ConfigNode merge(ConfigNode parent, ConfigNode child) {
        if (parent == null) {
            return child != null ? child : new ConfigNode();
        }
        if (child == null) {
            return new ConfigNode(parent.getData());
        }

        ConfigNode result = new ConfigNode();
        result.putAll(parent.getData());

        for (Map.Entry<String, Object> entry : child.getData().entrySet()) {
            String key = entry.getKey();
            Object childValue = entry.getValue();
            Object parentValue = parent.get(key);

            Object resolvedValue = resolveValue(childValue, parentValue, key);
            result.put(key, resolvedValue);
        }

        return result;
    }

    public Object resolveValue(Object value, Object parentValue, String key) {
        if (value == null) {
            return parentValue;
        }

        if (value instanceof String) {
            String strValue = (String) value;
            return resolveStringExpression(strValue, parentValue);
        }

        if (value instanceof Map && parentValue instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> childMap = (Map<String, Object>) value;
            @SuppressWarnings("unchecked")
            Map<String, Object> parentMap = (Map<String, Object>) parentValue;
            return deepMergeMaps(parentMap, childMap);
        }

        if (value instanceof List && parentValue instanceof List) {
            return value;
        }

        return value;
    }

    private Object resolveStringExpression(String strValue, Object parentValue) {
        Matcher inheritMatcher = INHERIT_PATTERN.matcher(strValue);
        if (inheritMatcher.matches()) {
            if (parentValue != null) {
                return parentValue;
            }
            String defaultValue = inheritMatcher.group(1);
            return defaultValue != null ? defaultValue : null;
        }

        if (MERGE_PATTERN.matcher(strValue).matches()) {
            return parentValue;
        }

        if (APPEND_PATTERN.matcher(strValue).matches()) {
            return parentValue;
        }

        return strValue;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> deepMergeMaps(Map<String, Object> base, Map<String, Object> overlay) {
        Map<String, Object> result = new LinkedHashMap<>(base);

        for (Map.Entry<String, Object> entry : overlay.entrySet()) {
            String key = entry.getKey();
            Object overlayValue = entry.getValue();

            if (result.containsKey(key)) {
                Object baseValue = result.get(key);

                if (overlayValue instanceof String) {
                    String strValue = (String) overlayValue;
                    Object resolved = resolveStringExpression(strValue, baseValue);
                    if (resolved != null && !strValue.equals(resolved)) {
                        result.put(key, resolved);
                        continue;
                    }
                }

                if (baseValue instanceof Map && overlayValue instanceof Map) {
                    result.put(key, deepMergeMaps((Map<String, Object>) baseValue, (Map<String, Object>) overlayValue));
                } else {
                    result.put(key, overlayValue);
                }
            } else {
                result.put(key, overlayValue);
            }
        }

        return result;
    }

    public ConfigNode applyOverrides(ConfigNode baseConfig, Map<String, Object> overrides) {
        if (overrides == null || overrides.isEmpty()) {
            return baseConfig;
        }

        ConfigNode result = new ConfigNode();
        result.putAll(baseConfig.getData());

        for (Map.Entry<String, Object> entry : overrides.entrySet()) {
            String path = entry.getKey();
            Object value = entry.getValue();
            result.putNested(path, value);
        }

        return result;
    }

    public Map<String, Object> extractOverrides(ConfigNode baseConfig, ConfigNode overrideConfig) {
        Map<String, Object> overrides = new LinkedHashMap<>();
        
        if (baseConfig == null || overrideConfig == null) {
            return overrides;
        }

        extractOverridesRecursive("", baseConfig.getData(), overrideConfig.getData(), overrides);
        
        return overrides;
    }

    @SuppressWarnings("unchecked")
    private void extractOverridesRecursive(String prefix, Map<String, Object> base, Map<String, Object> override, 
                                           Map<String, Object> result) {
        for (Map.Entry<String, Object> entry : override.entrySet()) {
            String key = entry.getKey();
            Object overrideValue = entry.getValue();
            String fullPath = prefix.isEmpty() ? key : prefix + "." + key;

            Object baseValue = base.get(key);

            if (overrideValue instanceof Map && baseValue instanceof Map) {
                extractOverridesRecursive(fullPath, (Map<String, Object>) baseValue, 
                    (Map<String, Object>) overrideValue, result);
            } else if (!Objects.equals(baseValue, overrideValue)) {
                result.put(fullPath, overrideValue);
            }
        }
    }
}
