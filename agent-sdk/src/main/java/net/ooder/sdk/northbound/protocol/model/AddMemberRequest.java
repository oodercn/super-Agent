package net.ooder.sdk.northbound.protocol.model;

public class AddMemberRequest {
    
    private String memberId;
    private String memberName;
    private String domainRole;
    
    public String getMemberId() { return memberId; }
    public void setMemberId(String memberId) { this.memberId = memberId; }
    
    public String getMemberName() { return memberName; }
    public void setMemberName(String memberName) { this.memberName = memberName; }
    
    public String getDomainRole() { return domainRole; }
    public void setDomainRole(String domainRole) { this.domainRole = domainRole; }
}
