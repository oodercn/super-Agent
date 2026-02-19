package net.ooder.sdk.northbound.protocol.model;

public interface DomainListener {
    
    void onDomainCreated(DomainInfo domain);
    
    void onDomainUpdated(DomainInfo domain);
    
    void onDomainDeleted(String domainId);
    
    void onMemberAdded(String domainId, DomainMember member);
    
    void onMemberRemoved(String domainId, String memberId);
    
    void onPolicyUpdated(String domainId, DomainPolicyConfig policy);
}
