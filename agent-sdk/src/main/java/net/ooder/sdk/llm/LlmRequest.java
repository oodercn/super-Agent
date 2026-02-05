package net.ooder.sdk.llm;

import java.util.List;
import java.util.Map;

public class LlmRequest {
    private String requestType;
    private SceneInfo sceneInfo;
    private Requirements requirements;

    public LlmRequest() {
    }

    public LlmRequest(String requestType, SceneInfo sceneInfo, Requirements requirements) {
        this.requestType = requestType;
        this.sceneInfo = sceneInfo;
        this.requirements = requirements;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public SceneInfo getSceneInfo() {
        return sceneInfo;
    }

    public void setSceneInfo(SceneInfo sceneInfo) {
        this.sceneInfo = sceneInfo;
    }

    public Requirements getRequirements() {
        return requirements;
    }

    public void setRequirements(Requirements requirements) {
        this.requirements = requirements;
    }

    public static class SceneInfo {
        private String sceneId;
        private String name;
        private List<MemberRole> memberRoles;

        public SceneInfo() {
        }

        public SceneInfo(String sceneId, String name, List<MemberRole> memberRoles) {
            this.sceneId = sceneId;
            this.name = name;
            this.memberRoles = memberRoles;
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

        public List<MemberRole> getMemberRoles() {
            return memberRoles;
        }

        public void setMemberRoles(List<MemberRole> memberRoles) {
            this.memberRoles = memberRoles;
        }
    }

    public static class MemberRole {
        private String roleId;
        private List<Agent> agents;

        public MemberRole() {
        }

        public MemberRole(String roleId, List<Agent> agents) {
            this.roleId = roleId;
            this.agents = agents;
        }

        public String getRoleId() {
            return roleId;
        }

        public void setRoleId(String roleId) {
            this.roleId = roleId;
        }

        public List<Agent> getAgents() {
            return agents;
        }

        public void setAgents(List<Agent> agents) {
            this.agents = agents;
        }
    }

    public static class Agent {
        private String agentId;
        private List<String> capabilities;
        private String techStack;

        public Agent() {
        }

        public Agent(String agentId, List<String> capabilities, String techStack) {
            this.agentId = agentId;
            this.capabilities = capabilities;
            this.techStack = techStack;
        }

        public String getAgentId() {
            return agentId;
        }

        public void setAgentId(String agentId) {
            this.agentId = agentId;
        }

        public List<String> getCapabilities() {
            return capabilities;
        }

        public void setCapabilities(List<String> capabilities) {
            this.capabilities = capabilities;
        }

        public String getTechStack() {
            return techStack;
        }

        public void setTechStack(String techStack) {
            this.techStack = techStack;
        }
    }

    public static class Requirements {
        private String communicationProtocol;
        private String securityLevel;
        private String errorHandling;
        private String outputFormat;

        public Requirements() {
        }

        public Requirements(String communicationProtocol, String securityLevel, String errorHandling, String outputFormat) {
            this.communicationProtocol = communicationProtocol;
            this.securityLevel = securityLevel;
            this.errorHandling = errorHandling;
            this.outputFormat = outputFormat;
        }

        public String getCommunicationProtocol() {
            return communicationProtocol;
        }

        public void setCommunicationProtocol(String communicationProtocol) {
            this.communicationProtocol = communicationProtocol;
        }

        public String getSecurityLevel() {
            return securityLevel;
        }

        public void setSecurityLevel(String securityLevel) {
            this.securityLevel = securityLevel;
        }

        public String getErrorHandling() {
            return errorHandling;
        }

        public void setErrorHandling(String errorHandling) {
            this.errorHandling = errorHandling;
        }

        public String getOutputFormat() {
            return outputFormat;
        }

        public void setOutputFormat(String outputFormat) {
            this.outputFormat = outputFormat;
        }
    }
}