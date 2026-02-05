package net.ooder.sdk.agent.scene;

import java.util.List;

public class SceneDefinition {
    private String sceneId;
    private String name;
    private String description;
    private String organizer;
    private List<MemberRole> memberRoles;
    private String communicationProtocol;
    private String securityPolicy;

    public SceneDefinition() {
    }

    public SceneDefinition(String sceneId, String name, String description, String organizer, List<MemberRole> memberRoles, String communicationProtocol, String securityPolicy) {
        this.sceneId = sceneId;
        this.name = name;
        this.description = description;
        this.organizer = organizer;
        this.memberRoles = memberRoles;
        this.communicationProtocol = communicationProtocol;
        this.securityPolicy = securityPolicy;
    }

    // Additional constructor for backward compatibility with tests
    public SceneDefinition(String sceneId, String name, List<MemberRole> memberRoles, String communicationProtocol, String securityPolicy) {
        this.sceneId = sceneId;
        this.name = name;
        this.description = "";
        this.organizer = "";
        this.memberRoles = memberRoles;
        this.communicationProtocol = communicationProtocol;
        this.securityPolicy = securityPolicy;
    }

    public String getSceneId() {
        return sceneId;
    }

    public void setSceneId(String sceneId) {
        this.sceneId = sceneId;
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

    public String getOrganizer() {
        return organizer;
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    public List<MemberRole> getMemberRoles() {
        return memberRoles;
    }

    public void setMemberRoles(List<MemberRole> memberRoles) {
        this.memberRoles = memberRoles;
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
}