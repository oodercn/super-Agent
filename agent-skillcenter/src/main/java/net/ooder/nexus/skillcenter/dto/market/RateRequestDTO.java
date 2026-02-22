package net.ooder.nexus.skillcenter.dto.market;

import net.ooder.nexus.skillcenter.dto.BaseDTO;

public class RateRequestDTO extends BaseDTO {

    private String id;
    private double rating;
    private String comment;
    private String userId;

    public RateRequestDTO() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
