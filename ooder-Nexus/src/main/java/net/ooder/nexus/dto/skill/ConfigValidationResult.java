package net.ooder.nexus.dto.skill;

import java.util.ArrayList;
import java.util.List;

public class ConfigValidationResult {

    private boolean valid;
    private List<String> errors;
    private List<String> warnings;
    private List<String> missingRequired;

    public ConfigValidationResult() {
        this.errors = new ArrayList<>();
        this.warnings = new ArrayList<>();
        this.missingRequired = new ArrayList<>();
    }

    public static ConfigValidationResult success() {
        ConfigValidationResult result = new ConfigValidationResult();
        result.setValid(true);
        return result;
    }

    public static ConfigValidationResult failure(List<String> errors) {
        ConfigValidationResult result = new ConfigValidationResult();
        result.setValid(false);
        result.setErrors(errors);
        return result;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public List<String> getWarnings() {
        return warnings;
    }

    public void setWarnings(List<String> warnings) {
        this.warnings = warnings;
    }

    public List<String> getMissingRequired() {
        return missingRequired;
    }

    public void setMissingRequired(List<String> missingRequired) {
        this.missingRequired = missingRequired;
    }

    public void addError(String error) {
        this.errors.add(error);
        this.valid = false;
    }

    public void addWarning(String warning) {
        this.warnings.add(warning);
    }

    public void addMissingRequired(String field) {
        this.missingRequired.add(field);
        this.valid = false;
    }
}
