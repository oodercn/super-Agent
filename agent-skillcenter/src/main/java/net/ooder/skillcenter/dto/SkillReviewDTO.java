package net.ooder.skillcenter.dto;

import java.util.Date;

/**
 * 技能评价数据传输对象
 */
public class SkillReviewDTO {

    private String id;
    private String skillId;
    private String userId;
    private String username;
    private double rating;
    private String comment;
    private Date createdAt;

    public SkillReviewDTO() {}

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getSkillId() { return skillId; }
    public void setSkillId(String skillId) { this.skillId = skillId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}
