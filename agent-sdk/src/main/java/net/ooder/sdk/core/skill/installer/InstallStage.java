
package net.ooder.sdk.core.skill.installer;

public interface InstallStage {
    
    String getName();
    
    void execute(InstallContext context) throws Exception;
}
