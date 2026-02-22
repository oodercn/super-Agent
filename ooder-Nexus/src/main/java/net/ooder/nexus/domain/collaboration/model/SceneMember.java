package net.ooder.nexus.domain.collaboration.model;

/**
 * 场景成员模型
 *
 * @author ooder Team
 * @version 0.7.3
 * @since SDK 0.7.3
 */
public class SceneMember {

    private String memberId;
    private String memberName;
    private String role;
    private long joinedAt;
    private boolean online;

    public enum Role {
        OWNER,
        ADMIN,
        MEMBER,
        GUEST
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public long getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(long joinedAt) {
        this.joinedAt = joinedAt;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }
}
