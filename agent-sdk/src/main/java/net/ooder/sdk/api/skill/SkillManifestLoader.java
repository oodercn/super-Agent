
package net.ooder.sdk.api.skill;

import java.io.InputStream;
import java.util.List;

public interface SkillManifestLoader {
    
    SkillManifest load(String manifestPath);
    
    SkillManifest load(InputStream inputStream);
    
    SkillManifest loadFromClasspath(String resourcePath);
    
    List<SkillManifest> loadAll();
    
    boolean validate(SkillManifest manifest);
}
