package net.ooder.nexus.skillcenter.dto.market;

import net.ooder.nexus.skillcenter.dto.BaseDTO;

public class DownloadResultDTO extends BaseDTO {

    private boolean success;
    private String skillId;
    private int dataSize;
    private String message;

    public DownloadResultDTO() {}

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

    public int getDataSize() {
        return dataSize;
    }

    public void setDataSize(int dataSize) {
        this.dataSize = dataSize;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
