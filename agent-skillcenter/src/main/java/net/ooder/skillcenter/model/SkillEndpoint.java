package net.ooder.skillcenter.model;

import javax.persistence.*;

@Entity
@Table(name = "skill_endpoints")
public class SkillEndpoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "path", nullable = false)
    private String path;

    @Column(name = "method", nullable = false)
    private String method;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "capability")
    private String capability;

    @Column(name = "authentication")
    private Boolean authentication;

    @Column(name = "rate_limit")
    private Integer rateLimit;

    @Column(name = "parameters", length = 1000)
    private String parameters; // JSON format

    @Column(name = "response", length = 1000)
    private String response; // JSON format

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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCapability() {
        return capability;
    }

    public void setCapability(String capability) {
        this.capability = capability;
    }

    public Boolean getAuthentication() {
        return authentication;
    }

    public void setAuthentication(Boolean authentication) {
        this.authentication = authentication;
    }

    public Integer getRateLimit() {
        return rateLimit;
    }

    public void setRateLimit(Integer rateLimit) {
        this.rateLimit = rateLimit;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public SkillPackage getSkillPackage() {
        return skillPackage;
    }

    public void setSkillPackage(SkillPackage skillPackage) {
        this.skillPackage = skillPackage;
    }

}
