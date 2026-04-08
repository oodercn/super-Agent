package net.ooder.mvp.skill.scene.dto.scene;

import java.util.List;

public class DataIsolationRuleDTO {
    private String domain;
    private List<String> access;

    public String getDomain() { return domain; }
    public void setDomain(String domain) { this.domain = domain; }
    public List<String> getAccess() { return access; }
    public void setAccess(List<String> access) { this.access = access; }
}
