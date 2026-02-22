package net.ooder.nexus.skillcenter.dto.admin;

import net.ooder.nexus.skillcenter.dto.BaseDTO;

public class AuthStatusUpdateDTO extends BaseDTO {

    private String status;
    private String reviewer;
    private String comments;

    public AuthStatusUpdateDTO() {}

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReviewer() {
        return reviewer;
    }

    public void setReviewer(String reviewer) {
        this.reviewer = reviewer;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
