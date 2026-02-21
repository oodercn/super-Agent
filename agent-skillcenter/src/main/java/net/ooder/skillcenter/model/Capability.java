package net.ooder.skillcenter.model;

import javax.persistence.*;

@Entity
@Table(name = "capabilities")
public class Capability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "capability_id", nullable = false)
    private String capabilityId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "category")
    private String category;

    @Column(name = "parameters", length = 1000)
    private String parameters; // JSON format

    @Column(name = "returns", length = 1000)
    private String returns; // JSON format

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

    public String getCapabilityId() {
        return capabilityId;
    }

    public void setCapabilityId(String capabilityId) {
        this.capabilityId = capabilityId;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public String getReturns() {
        return returns;
    }

    public void setReturns(String returns) {
        this.returns = returns;
    }

    public SkillPackage getSkillPackage() {
        return skillPackage;
    }

    public void setSkillPackage(SkillPackage skillPackage) {
        this.skillPackage = skillPackage;
    }

}
