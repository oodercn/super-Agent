package net.ooder.skillcenter.southbound;

public class GroupMember {
    private String memberId;
    private String memberName;
    private String role;
    private String status;
    private long joinedAt;

    public String getMemberId() { return memberId; }
    public void setMemberId(String memberId) { this.memberId = memberId; }

    public String getMemberName() { return memberName; }
    public void setMemberName(String memberName) { this.memberName = memberName; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public long getJoinedAt() { return joinedAt; }
    public void setJoinedAt(long joinedAt) { this.joinedAt = joinedAt; }
}
