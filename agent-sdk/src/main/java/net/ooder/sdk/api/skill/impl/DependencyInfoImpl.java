
package net.ooder.sdk.api.skill.impl;

import java.util.ArrayList;
import java.util.List;

import net.ooder.sdk.api.skill.DependencyInfo;
import net.ooder.sdk.api.skill.DependencyItem;

public class DependencyInfoImpl implements DependencyInfo {
    
    private String skillId;
    private List<DependencyItem> dependencies = new ArrayList<>();
    private List<DependencyItem> missing = new ArrayList<>();
    private List<DependencyItem> outdated = new ArrayList<>();
    
    public DependencyInfoImpl() {
    }
    
    public DependencyInfoImpl(String skillId) {
        this.skillId = skillId;
    }
    
    @Override
    public String getSkillId() {
        return skillId;
    }
    
    public void setSkillId(String skillId) {
        this.skillId = skillId;
    }
    
    @Override
    public List<DependencyItem> getDependencies() {
        return dependencies;
    }
    
    public void setDependencies(List<DependencyItem> dependencies) {
        this.dependencies = dependencies != null ? dependencies : new ArrayList<>();
        recalculateStatus();
    }
    
    @Override
    public List<DependencyItem> getMissing() {
        return missing;
    }
    
    @Override
    public List<DependencyItem> getOutdated() {
        return outdated;
    }
    
    @Override
    public boolean isSatisfied() {
        return missing.isEmpty() && outdated.isEmpty();
    }
    
    public void addDependency(DependencyItem item) {
        if (item != null) {
            dependencies.add(item);
            updateStatusLists(item);
        }
    }
    
    private void recalculateStatus() {
        missing.clear();
        outdated.clear();
        for (DependencyItem item : dependencies) {
            updateStatusLists(item);
        }
    }
    
    private void updateStatusLists(DependencyItem item) {
        if (item.isMissing()) {
            missing.add(item);
        } else if (item.isOutdated()) {
            outdated.add(item);
        }
    }
}
