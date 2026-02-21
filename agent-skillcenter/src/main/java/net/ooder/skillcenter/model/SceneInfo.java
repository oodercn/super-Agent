package net.ooder.skillcenter.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "scene_infos")
public class SceneInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "communication_protocol")
    private String communicationProtocol;

    @Column(name = "security_policy")
    private String securityPolicy;

    @Column(name = "allow_parallel")
    private Boolean allowParallel;

    @ManyToOne
    @JoinColumn(name = "skill_package_id", nullable = false)
    private SkillPackage skillPackage;

    @ElementCollection
    @CollectionTable(name = "scene_capabilities", joinColumns = @JoinColumn(name = "scene_id"))
    @Column(name = "capability")
    private List<String> capabilities;

    @ElementCollection
    @CollectionTable(name = "scene_roles", joinColumns = @JoinColumn(name = "scene_id"))
    @Column(name = "role")
    private List<String> roles;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCommunicationProtocol() {
        return communicationProtocol;
    }

    public void setCommunicationProtocol(String communicationProtocol) {
        this.communicationProtocol = communicationProtocol;
    }

    public String getSecurityPolicy() {
        return securityPolicy;
    }

    public void setSecurityPolicy(String securityPolicy) {
        this.securityPolicy = securityPolicy;
    }

    public Boolean getAllowParallel() {
        return allowParallel;
    }

    public void setAllowParallel(Boolean allowParallel) {
        this.allowParallel = allowParallel;
    }

    public SkillPackage getSkillPackage() {
        return skillPackage;
    }

    public void setSkillPackage(SkillPackage skillPackage) {
        this.skillPackage = skillPackage;
    }

    public List<String> getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(List<String> capabilities) {
        this.capabilities = capabilities;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

}
