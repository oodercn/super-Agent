package net.ooder.nexus.skillcenter.dto.personal;

import net.ooder.nexus.skillcenter.dto.BaseDTO;
import java.util.List;

public class FeatureFlagsDTO extends BaseDTO {

    private boolean skillPublishing;
    private boolean skillMarket;
    private boolean skillExecution;
    private boolean groupManagement;
    private boolean skillAuthentication;
    private boolean personalIdentity;
    private List<String> featureList;

    public FeatureFlagsDTO() {}

    public boolean isSkillPublishing() {
        return skillPublishing;
    }

    public void setSkillPublishing(boolean skillPublishing) {
        this.skillPublishing = skillPublishing;
    }

    public boolean isSkillMarket() {
        return skillMarket;
    }

    public void setSkillMarket(boolean skillMarket) {
        this.skillMarket = skillMarket;
    }

    public boolean isSkillExecution() {
        return skillExecution;
    }

    public void setSkillExecution(boolean skillExecution) {
        this.skillExecution = skillExecution;
    }

    public boolean isGroupManagement() {
        return groupManagement;
    }

    public void setGroupManagement(boolean groupManagement) {
        this.groupManagement = groupManagement;
    }

    public boolean isSkillAuthentication() {
        return skillAuthentication;
    }

    public void setSkillAuthentication(boolean skillAuthentication) {
        this.skillAuthentication = skillAuthentication;
    }

    public boolean isPersonalIdentity() {
        return personalIdentity;
    }

    public void setPersonalIdentity(boolean personalIdentity) {
        this.personalIdentity = personalIdentity;
    }

    public List<String> getFeatureList() {
        return featureList;
    }

    public void setFeatureList(List<String> featureList) {
        this.featureList = featureList;
    }
}
