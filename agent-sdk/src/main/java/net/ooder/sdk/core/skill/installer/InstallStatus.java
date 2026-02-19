
package net.ooder.sdk.core.skill.installer;

public enum InstallStatus {
    
    INITIALIZED("initialized", "Installation initialized"),
    VALIDATING("validating", "Validating install request"),
    DOWNLOADING("downloading", "Downloading skill package"),
    EXTRACTING("extracting", "Extracting skill package"),
    CONFIGURING("configuring", "Configuring skill"),
    VERIFYING("verifying", "Verifying installation"),
    COMPLETED("completed", "Installation completed"),
    FAILED("failed", "Installation failed"),
    UNINSTALLING("uninstalling", "Uninstalling skill"),
    UNINSTALLED("uninstalled", "Skill uninstalled");
    
    private final String code;
    private final String description;
    
    InstallStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }
    
    public String getCode() { return code; }
    public String getDescription() { return description; }
}
