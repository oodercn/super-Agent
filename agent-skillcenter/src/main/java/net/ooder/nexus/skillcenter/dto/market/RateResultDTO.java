package net.ooder.nexus.skillcenter.dto.market;

import net.ooder.nexus.skillcenter.dto.BaseDTO;

public class RateResultDTO extends BaseDTO {

    private boolean success;
    private String skillId;
    private double rating;
    private String message;

    public RateResultDTO() {}

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getSkillId() {
        return skillId;
    }

    public void setSkillId(String skillId) {
        this.skillId = skillId;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
