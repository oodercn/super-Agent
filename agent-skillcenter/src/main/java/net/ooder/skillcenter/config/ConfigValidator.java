package net.ooder.skillcenter.config;

import net.ooder.skillcenter.config.model.ConfigItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 配置验证器 - 符合v0.7.0协议规范
 */
@Component
public class ConfigValidator {

    private static final Logger log = LoggerFactory.getLogger(ConfigValidator.class);

    public ValidationResult validate(List<ConfigItem> items, Map<String, Object> values) {
        ValidationResult result = new ValidationResult();
        
        if (items == null || items.isEmpty()) {
            result.setValid(true);
            return result;
        }

        for (ConfigItem item : items) {
            String name = item.getName();
            Object value = values != null ? values.get(name) : null;
            
            if (item.isRequired() && value == null && item.getDefaultValue() == null) {
                result.addError(name, "Required configuration missing: " + name);
                continue;
            }
            
            if (value != null && !item.validate(value)) {
                result.addError(name, "Invalid value for " + name + ": " + value);
                continue;
            }
            
            if (item.isSecret() && value != null) {
                log.debug("Secret configuration {} validated (value hidden)", name);
            } else {
                log.debug("Configuration {} validated: {}", name, value);
            }
        }

        result.setValid(result.getErrors().isEmpty());
        return result;
    }

    public Map<String, Object> applyDefaults(List<ConfigItem> items, Map<String, Object> values) {
        Map<String, Object> result = new HashMap<>();
        
        if (values != null) {
            result.putAll(values);
        }
        
        if (items != null) {
            for (ConfigItem item : items) {
                if (!result.containsKey(item.getName()) && item.getDefaultValue() != null) {
                    result.put(item.getName(), item.getDefaultValue());
                }
            }
        }
        
        return result;
    }

    public Map<String, Object> filterSecrets(List<ConfigItem> items, Map<String, Object> values) {
        Map<String, Object> result = new HashMap<>();
        Set<String> secretNames = new HashSet<>();
        
        if (items != null) {
            for (ConfigItem item : items) {
                if (item.isSecret()) {
                    secretNames.add(item.getName());
                }
            }
        }
        
        if (values != null) {
            for (Map.Entry<String, Object> entry : values.entrySet()) {
                if (!secretNames.contains(entry.getKey())) {
                    result.put(entry.getKey(), entry.getValue());
                }
            }
        }
        
        return result;
    }

    public static class ValidationResult {
        private boolean valid;
        private Map<String, String> errors;

        public ValidationResult() {
            this.errors = new LinkedHashMap<>();
        }

        public boolean isValid() { return valid; }
        public void setValid(boolean valid) { this.valid = valid; }

        public Map<String, String> getErrors() { return errors; }
        public void setErrors(Map<String, String> errors) { this.errors = errors; }

        public void addError(String field, String message) {
            errors.put(field, message);
        }

        public String getFirstError() {
            return errors.isEmpty() ? null : errors.values().iterator().next();
        }
    }
}
