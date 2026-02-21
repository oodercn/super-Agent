package net.ooder.skillcenter.model;

import javax.persistence.*;

@Entity
@Table(name = "skill_configs")
public class SkillConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "required")
    private Boolean required;

    @Column(name = "secret")
    private Boolean secret;

    @Column(name = "default_value", length = 1000)
    private String defaultValue;

    @Column(name = "validation")
    private String validation;

    @ManyToOne
    @JoinColumn(name = "skill_package_id", nullable = false)
    private SkillPackage skillPackage;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public Boolean getSecret() {
        return secret;
    }

    public void setSecret(Boolean secret) {
        this.secret = secret;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getValidation() {
        return validation;
    }

    public void setValidation(String validation) {
        this.validation = validation;
    }

    public SkillPackage getSkillPackage() {
        return skillPackage;
    }

    public void setSkillPackage(SkillPackage skillPackage) {
        this.skillPackage = skillPackage;
    }

}
