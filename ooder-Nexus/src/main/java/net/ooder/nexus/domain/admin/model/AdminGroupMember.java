package net.ooder.nexus.domain.admin.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "admin_group_member")
public class AdminGroupMember implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String groupId;

    @Column(nullable = false)
    private String userId;

    private String userName;
    private Long joinTime;

    public AdminGroupMember() {
    }

    public AdminGroupMember(String groupId, String userId) {
        this.groupId = groupId;
        this.userId = userId;
        this.joinTime = System.currentTimeMillis();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getGroupId() { return groupId; }
    public void setGroupId(String groupId) { this.groupId = groupId; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public Long getJoinTime() { return joinTime; }
    public void setJoinTime(Long joinTime) { this.joinTime = joinTime; }
}
